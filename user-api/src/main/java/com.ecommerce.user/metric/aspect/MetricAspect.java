package com.ecommerce.user.metric.aspect;

import com.ecommerce.user.metric.annotation.TimedMetric;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    private final MeterRegistry meterRegistry;

    @Around("@annotation(timedMetric)")
    public Object handleTimedMetric(ProceedingJoinPoint joinPoint, TimedMetric timedMetric) throws Throwable {
        String metricPrefix = timedMetric.value();
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            Object result = joinPoint.proceed(); // 메서드 실행
            meterRegistry.counter(metricPrefix + "_success").increment();
            return result;
        } catch (Exception e) {
            meterRegistry.counter(metricPrefix + "_failure").increment();
            log.error("Exception during {}: {}", metricPrefix, e.getMessage(), e);
            throw e;
        } finally {
            sample.stop(meterRegistry.timer(metricPrefix + "_duration"));
        }
    }
}
