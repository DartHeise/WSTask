package com.ws.task.logging;

import ch.qos.logback.classic.Logger;
import com.ws.task.util.LogAppender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiRequestLoggingAspectTest {

    private final ApiRequestLoggingAspect apiRequestLoggingAspect = new ApiRequestLoggingAspect();

    private final MockHttpServletRequest request = new MockHttpServletRequest();

    private final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

    private String clientIP;

    private LogAppender apiRequestLogAppender;

    @BeforeEach
    void setUp() {
        apiRequestLogAppender = new LogAppender();
        apiRequestLogAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(ApiRequestLoggingAspect.class);
        logger.addAppender(apiRequestLogAppender);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        clientIP = request.getRemoteAddr();
    }

    @Test
    void loggingRequest() throws Throwable {
        // Arrange
        Object response = "response";
        when(joinPoint.proceed()).thenReturn(response);

        // Act
        Object result = apiRequestLoggingAspect.loggingRequest(joinPoint);

        // Assert
        Assertions.assertEquals(response, result);

        assertApiRequestLog(null, null);
    }

    @Test
    void loggingRequestThrowsException() throws Throwable {
        // Arrange
        Throwable ex = new Throwable();
        when(joinPoint.proceed()).thenThrow(ex);

        // Act & Assert
        Assertions.assertThrows
                          (ex.getClass(), () -> apiRequestLoggingAspect.loggingRequest(joinPoint));

        assertApiRequestLog(null, null);

        assertApiRequestLogException(null, null);
    }

    private void assertApiRequestLog(String arguments, String callMethod) {
        String loggingMessage = String.format("client IP address: %s; call %s with arguments: %s",
                                              clientIP, arguments, callMethod);

        assertLog(loggingMessage);
    }

    private void assertApiRequestLogException(String callMethod, String exception) {
        String loggingExceptionMessage = String.format("Method Signature: %s, Exception: %s", callMethod, exception);

        assertLog(loggingExceptionMessage);
    }

    private void assertLog(String loggingMessage) {
        assertThat(apiRequestLogAppender.getLogEvents()).isNotEmpty()
                                                        .anySatisfy(event -> assertThat(event.getMessage())
                                                                .isEqualTo(loggingMessage));

        apiRequestLogAppender.stop();
    }
}
