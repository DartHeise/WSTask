package com.ws.task.model.post.mapper;

import com.ws.task.model.post.Post;
import com.ws.task.service.postService.arguments.PostArgument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface PostMapper {

    Post toPost(PostArgument postArgument, UUID id);
}
