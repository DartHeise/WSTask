package com.ws.task.service.postService;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class CreatePostArgument {

    private final String name;
}
