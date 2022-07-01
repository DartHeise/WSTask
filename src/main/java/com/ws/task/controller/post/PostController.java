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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final PostMapper postMapper;

    @GetMapping("/{id}")
    @ApiOperation("Получить должность по идентификатору")
    public PostDto getPost(@PathVariable UUID id) {
        Post post = postService.get(id);
        return postMapper.toPostDto(post);
    }

    @GetMapping("/getAll")
    @ApiOperation("Получить все должности")
    public List<PostDto> getAllPosts() {
        List<Post> posts = postService.getAll();
        return posts.stream()
                    .map(postMapper:: toPostDto)
                    .toList();
    }

    @PostMapping("/create")
    @ApiOperation("Добавить должность")
    public PostDto createPost(@RequestBody @Valid CreatePostDto createPostDto) {
        PostArgument postArgument = postMapper.toCreatePostArgument(createPostDto);
        Post createdPost = postService.create(postArgument);
        return postMapper.toPostDto(createdPost);
    }

    @PutMapping("/{id}/update")
    @ApiOperation("Обновить должность")
    public PostDto updatePost(@PathVariable UUID id, @RequestBody @Valid UpdatePostDto updatePostDto) {
        PostArgument postArgument = postMapper.toUpdatePostArgument(updatePostDto);
        Post updatedPost = postService.update(postArgument, id);
        return postMapper.toPostDto(updatedPost);
    }

    @DeleteMapping("/{id}/delete")
    @ApiOperation("Удалить должность")
    public void deletePost(@PathVariable UUID id) {
        postService.delete(id);
    }
}
