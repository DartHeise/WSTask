package com.ws.task.action;

import com.ws.task.controller.employee.dto.UpdateEmployeeDto;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.service.postService.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateEmployeeArgumentAction {

    private final PostService postService;

    public UpdateEmployeeArgument execute(UpdateEmployeeDto updateEmployeeDto) {
        return UpdateEmployeeArgument.builder()
                                     .firstName(updateEmployeeDto.getFirstName())
                                     .lastName(updateEmployeeDto.getLastName())
                                     .description(updateEmployeeDto.getDescription())
                                     .characteristics(updateEmployeeDto.getCharacteristics())
                                     .post(postService.get(updateEmployeeDto.getPostId()))
                                     .contacts(updateEmployeeDto.getContacts())
                                     .jobType(updateEmployeeDto.getJobType())
                                     .build();
    }
}
