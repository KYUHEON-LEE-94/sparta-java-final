package com.ecommerce.user.logging.aop;

import com.ecommerce.user.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessLogAspect {

    private final LogService logService;

    @Around("execution(* com.ecommerce.user.controller.*.*(..))")
    public Object logAccessEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long responseTime = end - start;

        String method = extractHttpMethod(joinPoint);
        String url = extractUrl(joinPoint);
        int statusCode = extractStatusCode(result);

        logService.logAccessEvent(null, method, url, statusCode, responseTime);

        return result;
    }

    private int extractStatusCode(Object result) {
        if (result instanceof ResponseEntity) {
            return ((ResponseEntity<?>) result).getStatusCodeValue();
        }
        return 200;
    }

    private String extractHttpMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(GetMapping.class)) return "GET";
        if (method.isAnnotationPresent(PostMapping.class)) return "POST";
        if (method.isAnnotationPresent(PutMapping.class)) return "PUT";
        if (method.isAnnotationPresent(DeleteMapping.class)) return "DELETE";

        return "UNKNOWN";
    }

    private String extractUrl(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String classPath = "";
        RequestMapping classMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        if (classMapping != null && classMapping.value().length > 0) {
            classPath = classMapping.value()[0];
        }

        String methodPath = "";
        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            methodPath = getMapping.value().length > 0 ? getMapping.value()[0] : "";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            methodPath = postMapping.value().length > 0 ? postMapping.value()[0] : "";
        }

        return classPath + methodPath;
    }
}
