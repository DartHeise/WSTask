package com.ws.task.action;

import com.ws.task.controller.employee.dto.UpdateEmployeeArgumentDto;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.service.postService.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateEmployeeArgumentAction {

    private final PostService postService;

    public UpdateEmployeeArgument execute(UpdateEmployeeArgumentDto updateEmployeeArgDto) {
        return UpdateEmployeeArgument.builder()
                .firstName(updateEmployeeArgDto.getFirstName())
                .lastName(updateEmployeeArgDto.getLastName())
                .description(updateEmployeeArgDto.getDescription())
                .characteristics(updateEmployeeArgDto.getCharacteristics())
                .post(postService.get(updateEmployeeArgDto.getPostId()))
                .contacts(updateEmployeeArgDto.getContacts())
                .jobType(updateEmployeeArgDto.getJobType())
                .build();
    }
}
