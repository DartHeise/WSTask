package com.ws.task.service.employeeService.arguments;

import com.ws.task.model.Post;
import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@Jacksonized
public class CreateEmployeeArgument {

    @NotBlank(message = "Необходимо указать имя")
    private final String firstName;

    @NotBlank(message = "Необходимо указать фамилию")
    private final String lastName;

    private final String description;

    @NotBlank(message = "Необходимо указать характеристику")
    private final List<String> characteristics;

    @NotNull(message = "Необходимо указать должность")
    private final Post post;

    private final JobType jobType;

    private final Contacts contacts;
}
