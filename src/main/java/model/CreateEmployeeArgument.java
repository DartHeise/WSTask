package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;
@Builder
@Getter
@JsonDeserialize(builder = CreateEmployeeArgument.CreateEmployeeArgumentBuilder.class)
public class CreateEmployeeArgument {

    @JsonProperty(value = "firstName")
    private final String firstName;

    @JsonProperty(value = "lastName")
    private final String lastName;

    @JsonProperty(value = "description")
    private final String description;

    @JsonProperty(value = "characteristics")
    private final List<String> characteristics;

    @JsonProperty(value = "postId")
    private final String postId;
}
