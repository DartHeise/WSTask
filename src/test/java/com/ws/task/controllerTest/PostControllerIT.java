package com.ws.task.controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.post.dto.CreatePostDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostDto;
import com.ws.task.controller.post.mapper.PostControllerMapper;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PostService postService;

    @Autowired
    private PostControllerMapper postControllerMapper;

    private List<Post> posts;

    private List<PostDto> expectedPostDtos;

    @BeforeEach
    private void setUp() throws IOException {
        postService.deleteAll();
        posts = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\post\\posts.json"),
                                                                                 new TypeReference<>() {});
        expectedPostDtos = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\post\\expectedPostDtos.json"),
                                                                                            new TypeReference<>() {});
        postService.addPosts(posts);
    }

    @Test
    void get() {
        // Arrange
        Post backendPost = posts.get(0);
        PostDto expectedPostDto = expectedPostDtos.get(0);

        // Act
        List<PostDto> response = webTestClient.get()
                                              .uri(uriBuilder -> uriBuilder.path("post/" + backendPost.getId().toString())
                                                                           .build())
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        PostDto actualPostDto = response.get(0);

        Assertions.assertEquals(expectedPostDto, actualPostDto);
    }

    @Test
    void getAll() {
        // Act
        List<PostDto> response = webTestClient.get()
                                              .uri(uriBuilder -> uriBuilder.path("post/getAll")
                                                                           .build())
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 3);

        PostDto backendResponse = response.get(0);
        Assertions.assertEquals(expectedPostDtos.get(0), backendResponse);

        PostDto frontendResponse = response.get(1);
        Assertions.assertEquals(expectedPostDtos.get(1), frontendResponse);

        PostDto fullstackResponse = response.get(2);
        Assertions.assertEquals(expectedPostDtos.get(2), fullstackResponse);
    }

    @Test
    void create() throws IOException {
        // Arrange
        CreatePostDto createPostDto = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\post\\postForCreate.json"),
                                                                                              CreatePostDto.class);

        // Act
        List<PostDto> response = webTestClient.post()
                                              .uri(uriBuilder -> uriBuilder.path("post/create")
                                                                           .build())
                                              .bodyValue(createPostDto)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        PostDto actualPostDto = response.get(0);
        PostDto createdPost = postControllerMapper.toPostDto(postService.get(actualPostDto.getId()));

        Assertions.assertEquals(createdPost, actualPostDto);
    }

    @Test
    void update() throws IOException {
        // Arrange
        UUID updatedId = posts.get(0).getId();
        UpdatePostDto updatePostDto = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\post\\postForUpdate.json"),
                                                                                              UpdatePostDto.class);

        // Act
        List<PostDto> response = webTestClient.put()
                                              .uri(uriBuilder -> uriBuilder.path("post/update/" + updatedId)
                                                                           .build())
                                              .bodyValue(updatePostDto)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        PostDto actualPostDto = response.get(0);
        PostDto updatedPost = postControllerMapper.toPostDto(postService.get(updatedId));

        Assertions.assertEquals(updatedPost, actualPostDto);
    }

    @Test
    void delete() {
        // Arrange
        Post deletedPost = posts.get(0);

        // Act
        webTestClient.delete()
                     .uri(uriBuilder -> uriBuilder.path("post/delete/" + deletedPost.getId())
                                                  .build())
                     .exchange()

                     // Assert
                     .expectStatus()
                     .isOk();

        Assertions.assertEquals(2, postService.getAll().size());
    }
}
