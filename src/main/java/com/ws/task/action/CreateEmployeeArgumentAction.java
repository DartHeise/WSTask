package com.ws.task.action;

import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.postService.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateEmployeeArgumentAction {

    private final PostService postService;

    public CreateEmployeeArgument execute(CreateEmployeeDto createEmployeeDto) {
        return CreateEmployeeArgument.builder()
                                     .firstName(createEmployeeDto.getFirstName())
                                     .lastName(createEmployeeDto.getLastName())
                                     .description(createEmployeeDto.getDescription())
                                     .characteristics(createEmployeeDto.getCharacteristics())
                                     .post(postService.get(createEmployeeDto.getPostId()))
                                     .contacts(createEmployeeDto.getContacts())
                                     .jobType(createEmployeeDto.getJobType())
                                     .build();
    }
}
