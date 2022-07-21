package com.ws.task.model.employee;

import com.ws.task.model.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    private String description;

    @ElementCollection
    private List<String> characteristics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Embedded
    private Contacts contacts;

    public String getName() {
        return lastName + " " + firstName;
    }
}
