package com.ws.task.controller.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Builder
@Jacksonized
@Data
public class CreatePostArgumentDto {

    private final UUID id;

    private final String name;
}
