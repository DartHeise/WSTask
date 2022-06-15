package com.ws.task.controller.post.mapper;

import com.ws.task.controller.post.dto.CreatePostArgumentDto;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatePostArgumentMapper {

    CreatePostArgument toCreatePostArgument(CreatePostArgumentDto createEmployeeArgDto);
}
