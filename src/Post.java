import java.util.UUID;

public class Post {
    private final UUID id;
    private final String postName;
    public Post(UUID id, String postName){
        this.id = id;
        this.postName = postName;
    }
    public String getPostName(){
        return postName;
    }
}
