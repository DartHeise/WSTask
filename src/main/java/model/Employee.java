package model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Formatter;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Employee{

    @NotBlank(message = "Необходимо указать имя")
    private final String firstName;

    @NotBlank(message = "Необходимо указать фамилию")
    private final String lastName;

    private final String description;

    @NotBlank(message = "Необходимо указать характеристику")
    private final List<String> characteristics;

    @NotBlank(message = "Необходимо указать должность")
    private final Post post;

    public String getName(){
        return lastName + " " + firstName;
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        f.format("Name: %s\n"
                + "About me: %s\n"
                + "Characteristics: %s\n"
                + "Post: %s\n",
                getName(), description, String.join(", ", characteristics), post.getName());
        return f.toString();
    }
}
