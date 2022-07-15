package com.ws.task.controller.exceptionHandler;

import com.ws.task.controller.exceptionHandler.dto.ErrorDto;
import com.ws.task.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return ErrorDto.builder()
                       .message(e.getMessage())
                       .build();
    }
}
