package com.ws.task.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "logging", name = "controller")
public class ApiRequestLoggingAspect {

    @Pointcut("within(com.ws.task.controller..*) && " +
              "!within(com.ws.task.controller.post.mapper..*) &&" +
              "!within(com.ws.task.controller.employee.mapper..*)")
    private void anyController() {

    }

    @Around("anyController()")
    public Object loggingRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            logRequest(joinPoint);
            return joinPoint.proceed();
        } catch (Throwable ex) {
            logError(joinPoint, ex);
            throw ex;
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("client IP address: ")
                     .append(getClientIP())
                     .append("; call ")
                     .append(joinPoint.getSignature())
                     .append(" with arguments: ")
                     .append(Arrays.toString(joinPoint.getArgs()));

        log.info(stringBuilder.toString());
    }

    private String getClientIP() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        return request.getRemoteAddr();
    }

    private void logError(ProceedingJoinPoint joinPoint, Throwable ex) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Method Signature: ")
                     .append(joinPoint.getSignature())
                     .append(", Exception: ")
                     .append(ex.getMessage());

        log.error(stringBuilder.toString());
    }
}
