package com.ws.task.controller.post.mapper;

import com.ws.task.controller.post.dto.CreatePostDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostDto;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import org.mapstruct.Mapper;

@Mapper
public interface PostControllerMapper {

    PostDto toPostDto(Post post);

    CreatePostArgument toUpdatePostArgument(CreatePostDto createPostDto);

    UpdatePostArgument toUpdatePostArgument(UpdatePostDto updatePostDto);
}
