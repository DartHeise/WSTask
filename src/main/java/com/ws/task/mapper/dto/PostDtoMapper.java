package com.ws.task.mapper.dto;

import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostDtoMapper {

    PostDto toPostDto(Post post);
}
