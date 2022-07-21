package com.ws.task.service.postService;

import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.post.Post;
import com.ws.task.repository.PostRepository;
import com.ws.task.service.postService.arguments.PostArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Post get(UUID id) {
        return postRepository.findById(id)
                             .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    @Transactional(readOnly = true)
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post create(PostArgument postArgument) {
        Post createdPost = postMapper.toPost(postArgument);

        return postRepository.save(createdPost);
    }

    @Transactional
    public Post update(PostArgument postArgument, UUID id) {
        log.debug("Updating post with id: {}", id);

        Post updatedPost = postRepository.findById(id)
                                         .orElseThrow(() -> new NotFoundException("Post not found"));

        updateFields(updatedPost, postArgument);

        return postRepository.save(updatedPost);
    }

    @Transactional
    public void delete(UUID id) {
        postRepository.deleteById(id);
    }

    private void updateFields(Post updatedPost, PostArgument postArgument) {
        log.info("Updating fields...");
        if (!Objects.equals(updatedPost.getName(), postArgument.getName())) {
            log.debug("name: {} -> {}", updatedPost.getName(), postArgument.getName());
            updatedPost.setName(postArgument.getName());
        }
    }
}
