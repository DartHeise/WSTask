package model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {

    @NotNull(message = "Необходимо указать идентификатор должности")
    private UUID id;

    @NotBlank(message = "Необходимо указать название должности")
    private String name;
}
