package com.example.DoroServer.global.util.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SecurityContextAspect {

    @After("execution(* com.example.DoroServer.domain..*Api.*(..)) || "
        + "@annotation(com.example.DoroServer.global.util.annotation.ClearSecurityContext)")
    public void ClearSecurityContext(JoinPoint joinPoint){
        log.info("시큐리티 컨텍스트 초기화");
        SecurityContextHolder.clearContext();
    }
}
