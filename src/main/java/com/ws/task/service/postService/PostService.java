package com.ws.task.service.postService;

import com.ws.task.exception.NotFoundException;
import com.ws.task.mapper.model.PostMapper;
import com.ws.task.mapper.model.PostMapperImpl;
import com.ws.task.model.Post;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    private final Map<UUID, Post> posts = new HashMap<>();

    private final PostMapper postMapper = new PostMapperImpl();

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

    public Post update(CreatePostArgument createPostArg, UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        Post post = postMapper.toPost(createPostArg, id);
        posts.replace(post.getId(), post);

        return post;
    }

    public void delete(UUID id) {
        throwNotFoundExceptionIfNotExists(id);

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
