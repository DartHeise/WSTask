package com.ws.task.service.postService;

import com.ws.task.exception.NotFoundException;
import com.ws.task.mapper.model.PostMapper;
import com.ws.task.mapper.model.PostMapperImpl;
import com.ws.task.model.Post;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    private final Map<UUID, Post> posts = new HashMap<>();

    private final PostMapper postMapper = new PostMapperImpl();

    public PostService() {
        posts.put(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"),
                  new Post(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"),
                                     "Frontend"));
    }

    public Post get(UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        return posts.get(id);
    }

    public List<Post> getAll() {
        return new ArrayList<>(posts.values());
    }

    public Post create(CreatePostArgument createPostArg) {
        Post post = postMapper.toPost(createPostArg, UUID.randomUUID());
        posts.put(post.getId(), post);

        return post;
    }

    public Post update(UpdatePostArgument updatePostArg, UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        Post post = postMapper.toPost(updatePostArg, id);
        posts.replace(post.getId(), post);

        return post;
    }

    public void delete(UUID id) {
        posts.remove(id);
    }

    public void deleteAll() {
        posts.clear();
    }

    private void throwNotFoundExceptionIfNotExists(UUID id) {
        if (!posts.containsKey(id))
            throw new NotFoundException();
    }
}
