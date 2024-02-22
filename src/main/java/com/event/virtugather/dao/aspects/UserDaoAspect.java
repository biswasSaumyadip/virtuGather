package com.event.virtugather.dao.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserDaoAspect {
    @Before("execution(* com.event.virtugather.dao.UserDao.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info("Before executing method: {} with arguments: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }


    @AfterThrowing("execution(* com.event.virtugather.dao.UserDao.*(..))")
    public void afterThrowingAdvice(JoinPoint joinPoint){
        log.error("Exception occurred in method: {} with arguments: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }
}
