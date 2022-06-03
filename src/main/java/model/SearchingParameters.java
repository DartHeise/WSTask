package model;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchingParameters {

    private String name;

    private UUID postId;
}
