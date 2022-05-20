import java.util.UUID;

public class Post {
    private final UUID id;
    private final String name;
    public Post(UUID id, String postName){
        this.id = id;
        this.name = postName;
    }
    public String getName(){
        return name;
    }
}
