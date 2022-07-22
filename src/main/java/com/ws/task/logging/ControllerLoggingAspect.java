package com.ws.task.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Pointcut("within(com.ws.task.controller.employee.EmployeeController) || " +
              "within(com.ws.task.controller.post.PostController)")
    private void anyController() {

    }

    @AfterThrowing(pointcut = "anyController()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Method Signature: {}", joinPoint.getSignature());
        log.error("Exception: {}", ex.getMessage());
    }

    @Before("anyController()")
    public void loggingRequest(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("client IP address: {}", request.getRemoteAddr());

        log.info("call {} with arguments: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }
}
