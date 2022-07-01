package com.ws.task.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.post.dto.CreatePostDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostDto;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.PostArgument;
import com.ws.task.service.postService.arguments.UpdatePostArgument;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    private List<Post> posts;

    private List<PostDto> expectedPostDtos;

    private SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    private void setUp() throws IOException {
        posts = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\posts.json"),
                                                                            new TypeReference<>() {});
        expectedPostDtos = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\expected_post_dtos.json"),
                                                                                         new TypeReference<>() {});
    }

    @Test
    void get() {
        // Arrange
        UUID backendPostId = posts.get(0).getId();
        Post backendPost = posts.get(0);

        when(postService.get(backendPostId)).thenReturn(backendPost);

        // Act
        PostDto response = webTestClient.get()
                                              .uri("post/{id}", backendPostId)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBody(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        PostDto expectedPostDto = expectedPostDtos.get(0);
        Assertions.assertEquals(expectedPostDto, response);
    }

    @Test
    void getAll() {
        // Arrange
        when(postService.getAll()).thenReturn(posts);

        // Act
        List<PostDto> response = webTestClient.get()
                                              .uri("post/getAll")
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        softAssertions.assertThat(response.size()).isEqualTo(3);

        PostDto backendResponse = response.get(0);
        softAssertions.assertThat(expectedPostDtos.get(0)).isEqualTo(backendResponse);

        PostDto frontendResponse = response.get(1);
        softAssertions.assertThat(expectedPostDtos.get(1)).isEqualTo(frontendResponse);

        PostDto fullstackResponse = response.get(2);
        softAssertions.assertThat(expectedPostDtos.get(2)).isEqualTo(fullstackResponse);
    }

    @Test
    void create() throws IOException {
        // Arrange
        CreatePostDto createPostDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\create_post_dto.json"),
                                                                                           CreatePostDto.class);
        PostArgument createPostArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\create_post_argument.json"),
                                                                                           CreatePostArgument.class);
        Post createdPost = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\created_post.json"),
                                                                                                 Post.class);

        when(postService.create(createPostArgument)).thenReturn(createdPost);

        // Act
        PostDto response = webTestClient.post()
                                              .uri("post/create")
                                              .bodyValue(createPostDto)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBody(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        PostDto expectedPostDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\create_expected.json"),
                                                                                                 PostDto.class);
        Assertions.assertEquals(expectedPostDto, response);
    }

    @Test
    void update() throws IOException {
        // Arrange
        UUID updatedId = posts.get(0).getId();

        UpdatePostDto updatePostDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\update_post_dto.json"),
                                                                                           UpdatePostDto.class);
        PostArgument updatePostArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\update_post_argument.json"),
                                                                                           UpdatePostArgument.class);
        Post updatedPost = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\updated_post.json"),
                                                                                                 Post.class);

        when(postService.update(updatePostArgument, updatedId)).thenReturn(updatedPost);

        // Act
        PostDto response = webTestClient.put()
                                              .uri("post/{id}/update", updatedId)
                                              .bodyValue(updatePostDto)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBody(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        PostDto expectedPostDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\post\\update_expected.json"),
                                                                                                 PostDto.class);
        Assertions.assertEquals(expectedPostDto, response);
    }

    @Test
    void delete() {
        // Arrange
        UUID deletedPostId = posts.get(0).getId();

        // Act
        webTestClient.delete()
                     .uri("post/{id}/delete", deletedPostId)
                     .exchange()

                     // Assert
                     .expectStatus()
                     .isOk();
    }
}
