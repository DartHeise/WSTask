package com.ws.task.service.postService;

import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.post.Post;
import com.ws.task.repository.PostRepository;
import com.ws.task.service.postService.arguments.PostArgument;
import com.ws.task.util.Guard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    private final PostRepository postRepository;

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Post get(UUID id) {
        return postRepository.findById(id)
                             .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post create(PostArgument postArgument) {
        Post createdPost = postMapper.toPost(postArgument);

        return postRepository.save(createdPost);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Post update(PostArgument postArgument, UUID id) {
        Guard.check(postRepository.existsById(id), "Post not found");

        Post updatedPost = postMapper.toPost(postArgument, id);

        return postRepository.save(updatedPost);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(UUID id) {
        postRepository.deleteById(id);
    }
}
