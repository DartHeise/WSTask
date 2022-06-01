package service;

import model.Post;

import java.util.*;

public class PostService {

    private final Map<UUID, Post> posts = new HashMap<>();

    public PostService() {
        createPosts();
    }

    public Post getPostByUUID (UUID uuid) {
        return posts.get(uuid);
    }

    private void createPosts() {
        posts.put(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"),
                new Post(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"), "Backend"));
        posts.put(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"),
                new Post(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"), "Frontend"));
        posts.put(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"),
                new Post(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"), "Fullstack"));
    }
}
