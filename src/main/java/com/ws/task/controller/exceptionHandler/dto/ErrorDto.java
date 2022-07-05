package com.ws.task.controller.exceptionHandler.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
public class ErrorDto {

    private String message;
}
