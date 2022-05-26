package model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CreateEmployeeArgument {

    private String firstName;

    private String lastName;

    private String description;

    private List<String> characteristics;

    private String postId;
}
