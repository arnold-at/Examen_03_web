package com.tecsup.examen_03_web.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PedidoAspect {

    private static final Logger log = LoggerFactory.getLogger(PedidoAspect.class);

    @Pointcut("execution(* com.tecsup.examen_03_web.controller.PedidoController.*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postControllerMethods() {}

    @Around("postControllerMethods()")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("AOP-PERFORMANCE: {}.{} ejecutado en {} ms", className, methodName, executionTime);
        }
        return result;
    }
}