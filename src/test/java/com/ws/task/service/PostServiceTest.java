package com.ws.task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.post.Post;
import com.ws.task.repository.PostRepository;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.PostArgument;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import com.ws.task.util.ReadValueAction;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(SoftAssertionsExtension.class)
public class PostServiceTest {

    private final PostMapper postMapper = mock(PostMapper.class);

    private final PostRepository postRepository = mock(PostRepository.class);

    private final PostService postService = new PostService(postMapper, postRepository);

    private final ReadValueAction readValueAction = new ReadValueAction();

    @Test
    public void getPostById() throws IOException {
        // Arrange
        Post expected = readValueAction.execute
                                               ("jsons\\service\\post\\post_for_get_post_by_id_test.json", Post.class);

        UUID backendId = expected.getId();

        when(postRepository.findById(backendId)).thenReturn(Optional.of(expected));

        // Act
        Post actual = postService.get(backendId);

        // Assert
        Assertions.assertEquals(expected, actual);

        verify(postRepository).findById(backendId);
    }

    @Test
    public void getAllPosts() throws IOException {
        // Arrange
        List<Post> expected = readValueAction.execute
                                                     ("jsons\\service\\post\\posts_for_get_all_posts_test.json", new TypeReference<>() {});

        when(postRepository.findAll()).thenReturn(expected);

        // Act
        List<Post> actual = postService.getAll();

        // Assert
        Assertions.assertEquals(expected, actual);

        verify(postRepository).findAll();
    }

    @Test
    public void getNotExistingPostById() {
        // Arrange
        UUID postId = UUID.randomUUID();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows
                                                        (NotFoundException.class, () -> postService.get(postId));

        String expectedMessage = "Post not found";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));

        verify(postRepository).findById(postId);
    }

    @Test
    public void createPost() throws IOException {
        // Arrange
        PostArgument postArgument = readValueAction.execute
                                                           ("jsons\\service\\post\\create_post_argument.json",
                                                            CreatePostArgument.class);

        Post postForCreate = readValueAction.execute
                                                    ("jsons\\service\\post\\post_for_create.json",
                                                     Post.class);

        Post createdPost = readValueAction.execute
                                                  ("jsons\\service\\post\\created_post.json",
                                                   Post.class);

        when(postMapper.toPost(postArgument)).thenReturn(postForCreate);
        when(postRepository.save(postForCreate)).thenReturn(createdPost);

        // Act
        Post actual = postService.create(postArgument);

        // Assert
        Assertions.assertEquals(createdPost, actual);

        verify(postRepository).save(postForCreate);
    }

    @Test
    public void updatePost() throws IOException {
        // Arrange
        PostArgument postArgument = readValueAction.execute
                                                           ("jsons\\service\\post\\post_for_update_test.json",
                                                            UpdatePostArgument.class);

        Post postForUpdate = readValueAction.execute
                                                    ("jsons\\service\\post\\post_for_update.json",
                                                     Post.class);

        Post oldPost = readValueAction.execute
                                              ("jsons\\service\\post\\old_post.json",
                                               Post.class);

        UUID updatedId = postForUpdate.getId();

        when(postMapper.toPost(postArgument, updatedId)).thenReturn(postForUpdate);
        when(postRepository.findById(updatedId)).thenReturn(Optional.of(oldPost));
        when(postRepository.save(postForUpdate)).thenReturn(postForUpdate);

        // Act
        Post actual = postService.update(postArgument, updatedId);

        // Assert
        Assertions.assertEquals(postForUpdate, actual);

        verify(postRepository).findById(updatedId);
        verify(postRepository).save(postForUpdate);
    }

    @Test
    public void deletePost() {
        // Arrange
        UUID deletedPostId = UUID.randomUUID();

        doNothing().when(postRepository).deleteById(deletedPostId);

        // Act
        postService.delete(deletedPostId);

        // Assert
        verify(postRepository).deleteById(deletedPostId);
    }
}