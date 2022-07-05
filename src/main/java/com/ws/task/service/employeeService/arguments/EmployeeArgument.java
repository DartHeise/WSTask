package com.ws.task.service.employeeService.arguments;

import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.JobType;
import com.ws.task.model.post.Post;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Value
@NonFinal
public abstract class EmployeeArgument {

    String firstName;

    String lastName;

    String description;

    List<String> characteristics;

    Post post;

    JobType jobType;

    Contacts contacts;
}
