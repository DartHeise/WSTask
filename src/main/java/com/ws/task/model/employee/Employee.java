package com.ws.task.model.employee;

import com.ws.task.model.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && Hibernate.getClass(this) == Hibernate.getClass(obj)) {
            Employee employee = (Employee) obj;
            return id.equals(employee.id)
                   && firstName.equals(employee.firstName)
                   && lastName.equals(employee.lastName)
                   && Objects.equals(description, employee.description)
                   && characteristics.equals(employee.characteristics)
                   && post.equals(employee.post)
                   && contacts.equals(employee.contacts)
                   && jobType == employee.jobType;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return lastName + " " + firstName;
    }
}
