import lombok.Getter;

import java.util.UUID;

public class Post {
    private final UUID id;
    @Getter
    private final String name;
    public Post(UUID id, String name){
        this.id = id;
        this.name = name;
    }
}
