package com.ws.task.model.post;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
}
