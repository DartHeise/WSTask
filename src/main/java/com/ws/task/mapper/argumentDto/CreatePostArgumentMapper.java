package com.ws.task.mapper.argumentDto;

import com.ws.task.controller.post.dto.CreatePostArgumentDto;
import com.ws.task.service.postService.CreatePostArgument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CreatePostArgumentMapper {

    CreatePostArgument toCreatePostArgument(CreatePostArgumentDto createEmployeeArgDto);
}
