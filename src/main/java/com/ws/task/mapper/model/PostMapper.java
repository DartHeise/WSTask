package com.ws.task.mapper.model;

import com.ws.task.service.postService.CreatePostArgument;
import com.ws.task.model.Post;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(CreatePostArgument createPostArgument, UUID id);
}
