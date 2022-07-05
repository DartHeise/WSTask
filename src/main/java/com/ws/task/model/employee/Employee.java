package com.ws.task.model.employee;

import com.ws.task.model.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {

    private UUID id;

    private String firstName;

    private String lastName;

    private String description;

    private List<String> characteristics;

    private Post post;

    private JobType jobType;

    private Contacts contacts;

    public String getName(){
        return lastName + " " + firstName;
    }
}
