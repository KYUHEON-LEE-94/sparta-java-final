package com.ecommerce.product.logging.aop;

import com.ecommerce.product.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AccessLogAspect {

    private final LogService logService;

    @Around("execution(* com.ecommerce.product.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // 메서드 실제 실행

        long end = System.currentTimeMillis();
        long responseTime = end - start;

        // === 로그 정보 구성 ===
        String method = "GET";
        String url = extractUrl(joinPoint);
        int statusCode = 0;
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            statusCode = responseEntity.getStatusCodeValue();
        } else {
            statusCode = 200;
        }

        logService.logAccessEvent(null, method, url, statusCode, responseTime);

        return result;
    }

    private String extractUrl(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestMapping classMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        String classPath = classMapping != null ? String.join("", classMapping.value()) : "";
        String methodPath = "";

        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            methodPath = getMapping.value().length > 0 ? getMapping.value()[0] : "";
        }


        return classPath + methodPath;
    }
}
