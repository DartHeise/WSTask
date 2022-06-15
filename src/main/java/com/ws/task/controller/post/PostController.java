package com.ws.task.controller.post;

import com.ws.task.controller.post.dto.CreatePostArgumentDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostArgumentDto;
import com.ws.task.controller.post.mapper.CreatePostArgumentMapper;
import com.ws.task.controller.post.mapper.UpdatePostArgumentMapper;
import com.ws.task.controller.post.mapper.PostDtoMapper;
import com.ws.task.model.Post;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
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

    private final PostDtoMapper postDtoMapper;

    private final CreatePostArgumentMapper createPostArgMapper;

    private final UpdatePostArgumentMapper updatePostArgMapper;

    @GetMapping("/get/{id}")
    @ApiOperation("Получить должность по идентификатору")
    public PostDto getPost(@PathVariable UUID id) {
        Post post = postService.get(id);
        return postDtoMapper.toPostDto(post);
    }

    @GetMapping("/getAll")
    @ApiOperation("Получить все должности")
    public List<PostDto> getAllPosts() {
        List<Post> posts = postService.getAll();
        return posts.stream()
                    .map(postDtoMapper :: toPostDto)
                    .toList();
    }

    @PostMapping("/create")
    @ApiOperation("Добавить должность")
    public PostDto createPost(@RequestBody @Valid CreatePostArgumentDto createPostArgDto) {
        CreatePostArgument createPostArg = createPostArgMapper.toCreatePostArgument(createPostArgDto);
        Post createdPost = postService.create(createPostArg);
        return postDtoMapper.toPostDto(createdPost);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("Обновить должность")
    public PostDto updatePost(@PathVariable UUID id, @RequestBody @Valid UpdatePostArgumentDto updatePostArgDto) {
        UpdatePostArgument updatePostArg = updatePostArgMapper.toUpdatePostArgument(updatePostArgDto);
        Post updatedPost = postService.update(updatePostArg, id);
        return postDtoMapper.toPostDto(updatedPost);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удалить должность")
    public void deletePost(@PathVariable UUID id) {
        postService.delete(id);
    }
}
