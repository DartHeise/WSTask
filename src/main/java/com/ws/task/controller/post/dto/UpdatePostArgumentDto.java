package com.ws.task.controller.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;


@Builder
@Jacksonized
@Data
public class UpdatePostArgumentDto {

    @NotBlank(message = "Необходимо указать название должности")
    private final String name;
}
