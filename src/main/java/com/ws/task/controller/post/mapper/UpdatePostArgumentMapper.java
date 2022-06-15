package com.ws.task.controller.post.mapper;

import com.ws.task.controller.post.dto.UpdatePostArgumentDto;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdatePostArgumentMapper {

    UpdatePostArgument toUpdatePostArgument(UpdatePostArgumentDto updateEmployeeArgDto);
}
