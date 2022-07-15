package com.ws.task.controller.post;

import com.ws.task.controller.post.dto.CreatePostDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostDto;
import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.PostArgument;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final PostMapper postMapper;

    @GetMapping("/{id}")
    @ApiOperation("Получить должность по идентификатору")
    public PostDto getPost(@PathVariable UUID id, HttpServletRequest request) {
        log.debug("Request getPost with params:");
        log.debug("id: {}", id);
        log.debug("request IP: {}", request.getRemoteAddr());
        Post post = postService.get(id);
        return postMapper.toPostDto(post);
    }

    @GetMapping("/getAll")
    @ApiOperation("Получить все должности")
    public List<PostDto> getAllPosts(HttpServletRequest request) {
        log.debug("Request getAllPosts with params:");
        log.debug("request IP: {}", request.getRemoteAddr());
        List<Post> posts = postService.getAll();
        return posts.stream()
                    .map(postMapper::toPostDto)
                    .collect(Collectors.toList());
    }

    @PostMapping("/create")
    @ApiOperation("Добавить должность")
    public PostDto createPost(@RequestBody @Valid CreatePostDto createPostDto, HttpServletRequest request) {
        log.debug("Request createPost with params:");
        log.debug("createPostDto: {}", createPostDto);
        log.debug("request IP: {}", request.getRemoteAddr());
        PostArgument postArgument = postMapper.toCreatePostArgument(createPostDto);
        Post createdPost = postService.create(postArgument);
        return postMapper.toPostDto(createdPost);
    }

    @PutMapping("/{id}/update")
    @ApiOperation("Обновить должность")
    public PostDto updatePost(@PathVariable UUID id, @RequestBody @Valid UpdatePostDto updatePostDto, HttpServletRequest request) {
        log.debug("Request updatePost with params:");
        log.debug("id: {}", id);
        log.debug("updatePostDto: {}", updatePostDto);
        log.debug("request IP: {}", request.getRemoteAddr());
        PostArgument postArgument = postMapper.toUpdatePostArgument(updatePostDto);
        Post updatedPost = postService.update(postArgument, id);
        return postMapper.toPostDto(updatedPost);
    }

    @DeleteMapping("/{id}/delete")
    @ApiOperation("Удалить должность")
    public void deletePost(@PathVariable UUID id, HttpServletRequest request) {
        log.debug("Request deletePost with params:");
        log.debug("id: {}", id);
        log.debug("request IP: {}", request.getRemoteAddr());
        postService.delete(id);
    }
}
