package com.ws.task.controller.employee.dto;

import com.ws.task.model.Post;
import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Builder
@Jacksonized
@Data
public class EmployeeDto {

    private final UUID id;

    private final String firstName;

    private final String lastName;

    private final String description;

    private final List<String> characteristics;

    private final Post post;

    private final JobType jobType;

    private final Contacts contacts;
}
