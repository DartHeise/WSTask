package com.ws.task.controller.exceptionHandler;

import com.ws.task.controller.exceptionHandler.dto.ErrorDto;
import com.ws.task.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleException(NotFoundException e) {
        return ErrorDto.builder()
                       .message(e.getMessage())
                       .build();
    }
}
