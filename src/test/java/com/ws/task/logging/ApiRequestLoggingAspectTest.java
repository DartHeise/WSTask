package com.ws.task.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiRequestLoggingAspectTest {

    @InjectMocks
    private final ApiRequestLoggingAspect apiRequestLoggingAspect = new ApiRequestLoggingAspect();
    @Mock
    private Logger logger;

    private final MockHttpServletRequest request = new MockHttpServletRequest();

    private final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
    private String clientIP;

    @BeforeEach
    void setUp() {
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

        verify(logger).info("client IP address: " + clientIP + "; call null with arguments: null");
        verifyNoMoreInteractions(logger);
    }

    @Test
    void loggingRequestThrowsException() throws Throwable {
        // Arrange
        Throwable ex = new Throwable();
        when(joinPoint.proceed()).thenThrow(ex);

        // Act & Assert
        Assertions.assertThrows
                          (ex.getClass(), () -> apiRequestLoggingAspect.loggingRequest(joinPoint));

        verify(logger).info("client IP address: " + clientIP + "; call null with arguments: null");
        verify(logger).error("Method Signature: null, Exception: null");
        verifyNoMoreInteractions(logger);
    }
}
