package com.ws.task.controller.employee.dto;

import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmployeeDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String description;

    private List<String> characteristics;

    private UUID postId;

    private JobType jobType;

    private Contacts contacts;
}
