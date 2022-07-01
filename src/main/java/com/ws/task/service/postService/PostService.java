package com.ws.task.service.postService;

import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.arguments.PostArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final Map<UUID, Post> posts;

    private final PostMapper postMapper;

    public void addPosts(List<Post> postList) {
        postList.stream().forEach(x -> posts.put(x.getId(), x));
    }

    public Post get(UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        return posts.get(id);
    }

    public List<Post> getAll() {
        return new ArrayList<>(posts.values());
    }

    public Post create(PostArgument postArgument) {
        Post post = postMapper.toPost(postArgument, UUID.randomUUID());
        posts.put(post.getId(), post);

        return post;
    }

    public Post update(PostArgument postArgument, UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        Post post = postMapper.toPost(postArgument, id);
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
            throw new NotFoundException("Post not found");
    }
}
