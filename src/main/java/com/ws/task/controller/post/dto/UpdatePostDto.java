package com.ws.task.controller.post.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePostDto {

    @NotBlank(message = "Необходимо указать название должности")
    private String name;
}
