package com.ws.task.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiRequestLoggingAspectUT {

    private final MockHttpServletRequest request = new MockHttpServletRequest();

    private final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

    private final ApiRequestLoggingAspect apiRequestLoggingAspect = new ApiRequestLoggingAspect();

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
    }

    @Test
    void loggingRequestThrowsException() throws Throwable {
        // Arrange
        Throwable ex = new Throwable();
        when(joinPoint.proceed()).thenThrow(ex);

        // Act & Assert
        Assertions.assertThrows
                          (ex.getClass(), () -> apiRequestLoggingAspect.loggingRequest(joinPoint));
    }
}
