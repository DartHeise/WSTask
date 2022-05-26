package model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Post {

    @NotBlank(message = "Необходимо указать идентификатор должности")
    private final UUID id;

    @NotBlank(message = "Необходимо указать название должности")
    private final String name;
}
