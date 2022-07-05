package com.ws.task.service.postService.arguments;

import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Value
@NonFinal
public abstract class PostArgument {

    String name;
}
