package com.ws.task.controller.employee.dto;

import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Builder
@Jacksonized
@Data
public class UpdateEmployeeDto {

    @NotBlank(message = "Необходимо указать имя")
    private final String firstName;

    @NotBlank(message = "Необходимо указать фамилию")
    private final String lastName;

    private final String description;

    @NotBlank(message = "Необходимо указать характеристику")
    private final List<String> characteristics;

    @NotNull(message = "Необходимо указать идентификатор должности")
    private final UUID postId;

    private final JobType jobType;

    private final Contacts contacts;
}
