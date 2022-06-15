package com.ws.task.controller.post.mapper;

import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostDtoMapper {

    PostDto toPostDto(Post post);
}
