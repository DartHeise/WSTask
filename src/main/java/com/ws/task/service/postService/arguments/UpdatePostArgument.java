package com.ws.task.service.postService.arguments;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class UpdatePostArgument {

    private final String name;
}
