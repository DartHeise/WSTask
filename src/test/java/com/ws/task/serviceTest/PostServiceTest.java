package com.ws.task.serviceTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.post.Post;
import com.ws.task.model.post.mapper.PostMapper;
import com.ws.task.model.post.mapper.PostMapperImpl;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.PostArgument;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
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

    @BeforeEach
    public void setUp() throws IOException {
        postService = new PostService(new HashMap<>(), new PostMapperImpl());
        posts = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\post\\posts.json"),
                                                                              new TypeReference<>() {});
        postService.addPosts(posts);
    }

    @Test
    public void getPostById() throws IOException {
        // Arrange
        Post expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\post\\postForGetPostByIdTest.json"),
                                                                                                             Post.class);
        Post actual;

        // Act
        actual = postService.get(posts.get(0).getId());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllPosts() throws IOException {
        // Arrange
        List<Post> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\post\\postsForGetAllPostsTest.json"),
                                                                                                new TypeReference<>() {});
        List<Post> actual;

        // Act
        actual = postService.getAll();

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getNotExistingPostById() throws IOException {
        // Arrange
        Post post = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\post\\postForGetNotExistingPostByIdTest.json"),
                                                                                                                        Post.class);
        NotFoundException exception;
        String expectedMessage = "Post not found";

        // Act & Assert
        exception = Assertions.assertThrows(NotFoundException.class, () -> postService.get(post.getId()));
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void createPost() throws IOException {
        // Arrange
        PostMapper postMapper = new PostMapperImpl();
        PostArgument postArgument = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\post\\postForCreateTest.json"),
                                                                                          CreatePostArgument.class);
        Post expected;
        Post actual;

        // Act
        actual = postService.create(postArgument);

        // Assert
        expected = postMapper.toPost(postArgument, actual.getId());
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(postService.getAll().size(), 4);
    }

    @Test
    public void updatePost() throws IOException {
        // Arrange
        PostMapper postMapper = new PostMapperImpl();
        PostArgument postArgument = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\post\\postForUpdateTest.json"),
                                                                                          UpdatePostArgument.class);
        Post expected;
        Post actual;

        UUID updatedId = posts.get(0).getId();

        // Act
        actual = postService.update(postArgument, updatedId);

        // Assert
        expected = postMapper.toPost(postArgument, updatedId);
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(postService.getAll().size(), 3);
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
