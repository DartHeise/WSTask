package com.ws.task.action;

import com.ws.task.controller.employee.dto.CreateEmployeeArgumentDto;
import com.ws.task.service.employeeService.CreateEmployeeArgument;
import com.ws.task.service.postService.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateEmployeeArgumentAction {

    private final PostService postService;

    public CreateEmployeeArgument execute(CreateEmployeeArgumentDto createEmployeeArgDto) {
        return CreateEmployeeArgument.builder()
                                     .firstName(createEmployeeArgDto.getFirstName())
                                     .lastName(createEmployeeArgDto.getLastName())
                                     .description(createEmployeeArgDto.getDescription())
                                     .characteristics(createEmployeeArgDto.getCharacteristics())
                                     .post(postService.get(createEmployeeArgDto.getPostId()))
                                     .contacts(createEmployeeArgDto.getContacts())
                                     .jobType(createEmployeeArgDto.getJobType())
                                     .build();
    }
}
