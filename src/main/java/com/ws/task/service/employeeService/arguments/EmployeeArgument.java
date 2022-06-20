package com.ws.task.service.employeeService.arguments;

import com.ws.task.model.post.Post;
import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public abstract class EmployeeArgument {

    private final String firstName;

    private final String lastName;

    private final String description;

    private final List<String> characteristics;

    private final Post post;

    private final JobType jobType;

    private final Contacts contacts;
}
