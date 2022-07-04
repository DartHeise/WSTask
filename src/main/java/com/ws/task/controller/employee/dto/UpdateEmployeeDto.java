package com.ws.task.controller.employee.dto;

import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateEmployeeDto {

    @NotBlank(message = "Необходимо указать имя")
    private String firstName;

    @NotBlank(message = "Необходимо указать фамилию")
    private String lastName;

    private String description;

    @NotNull(message = "Необходимо указать характеристику")
    private List<String> characteristics;

    @NotNull(message = "Необходимо указать идентификатор должности")
    private UUID postId;

    private JobType jobType;

    private Contacts contacts;
}
