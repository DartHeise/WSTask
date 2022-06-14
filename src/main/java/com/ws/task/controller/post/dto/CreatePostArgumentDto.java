package com.ws.task.controller.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
public class CreatePostArgumentDto {

    private final String name;
}
