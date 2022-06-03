package model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@Jacksonized
public class CreateEmployeeArgument {

    private final String firstName;

    private final String lastName;

    private final String description;

    private final List<String> characteristics;

    private final String postId;
}
