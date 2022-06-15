package com.ws.task.mapper.model;

import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.model.Post;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(CreatePostArgument createPostArgument, UUID id);

    Post toPost(UpdatePostArgument updatePostArgument, UUID id);
}
