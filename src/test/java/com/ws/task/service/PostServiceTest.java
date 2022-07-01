package com.ws.task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.controller.post.mapper.PostMapperImpl;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.PostArgument;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PostServiceTest {

    private PostService postService;

    private List<Post> posts;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SoftAssertions softAssertions = new SoftAssertions();

    private PostMapper postMapper = new PostMapperImpl();

    @BeforeEach
    public void setUp() throws IOException {
        postService = new PostService(new HashMap<>(), postMapper);
        posts = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\post\\posts.json"),
                                                                         new TypeReference<>() {});
        postService.addPosts(posts);
    }

    @Test
    public void getPostById() throws IOException {
        // Arrange
        Post expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\post\\post_for_get_post_by_id_test.json"),
                                                                                                              Post.class);
        UUID backendId = posts.get(0).getId();

        // Act
        Post actual = postService.get(backendId);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllPosts() throws IOException {
        // Arrange
        List<Post> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\post\\posts_for_get_all_posts_test.json"),
                                                                                                new TypeReference<>() {});

        // Act
        List<Post> actual = postService.getAll();

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getNotExistingPostById() throws IOException {
        // Arrange
        Post post = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\post\\post_for_get_not_existing_post_by_id_test.json"),
                                                                                                                           Post.class);

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows
                (NotFoundException.class, () -> postService.get(post.getId()));

        String expectedMessage = "Post not found";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void createPost() throws IOException {
        // Arrange
        PostArgument postArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\post\\post_for_create_test.json"),
                                                                                          CreatePostArgument.class);

        // Act
        Post actual = postService.create(postArgument);

        // Assert
        Post expected = postMapper.toPost(postArgument, actual.getId());
        softAssertions.assertThat(expected).isEqualTo(actual);

        softAssertions.assertThat(postService.getAll().size()).isEqualTo(4);
    }

    @Test
    public void updatePost() throws IOException {
        // Arrange
        PostArgument postArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\post\\post_for_update_test.json"),
                                                                                        UpdatePostArgument.class);
        UUID updatedId = posts.get(0).getId();

        // Act
        Post actual = postService.update(postArgument, updatedId);

        // Assert
        Post expected = postMapper.toPost(postArgument, updatedId);
        softAssertions.assertThat(expected).isEqualTo(actual);

        softAssertions.assertThat(postService.getAll().size()).isEqualTo(3);
    }

    @Test
    public void deletePost() {
        // Arrange
        UUID deletedPostId = posts.get(0).getId();

        // Act
        postService.delete(deletedPostId);

        // Assert
        Assertions.assertEquals(postService.getAll().size(), 2);
    }
}
