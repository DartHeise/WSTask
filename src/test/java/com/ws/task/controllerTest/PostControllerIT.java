package com.ws.task.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.post.dto.CreatePostArgumentDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostArgumentDto;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.Post;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PostService postService;

    private List<Post> posts;

    @BeforeEach
    private void setUp() throws IOException {
        postService.deleteAll();
        CreatePostArgument[] createPostArguments = new ObjectMapper().readValue
                (new File("src\\test\\java\\com\\ws\\task\\resources\\jsons\\controllerTests\\posts.json"), CreatePostArgument[].class);
        Arrays.stream(createPostArguments).forEach(x -> postService.create(x));
        posts = postService.getAll();
    }

    @Test
    void get() {
        // Arrange
        Post backendPost = posts.get(0);

        // Act
        List<PostDto> response = webTestClient.get()
                                              .uri(uriBuilder -> uriBuilder.path("post/get/" + backendPost.getId().toString())
                                                                           .build())
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        PostDto postDto = response.get(0);

        Assertions.assertEquals(postDto.getName(), backendPost.getName());
        Assertions.assertEquals(postDto.getId(), backendPost.getId());
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
        Assertions.assertEquals(backendResponse.getName(), posts.get(0).getName());
        Assertions.assertEquals(backendResponse.getId(), posts.get(0).getId());

        PostDto frontendResponse = response.get(1);
        Assertions.assertEquals(frontendResponse.getName(), posts.get(1).getName());
        Assertions.assertEquals(frontendResponse.getId(), posts.get(1).getId());

        PostDto fullstackResponse = response.get(2);
        Assertions.assertEquals(fullstackResponse.getName(), posts.get(2).getName());
        Assertions.assertEquals(fullstackResponse.getId(), posts.get(2).getId());
    }

    @Test
    void post() {
        // Arrange
        CreatePostArgumentDto createPostArg = CreatePostArgumentDto.builder()
                                                                   .name("Mobile")
                                                                   .build();

        // Act
        List<PostDto> response = webTestClient.post()
                                              .uri(uriBuilder -> uriBuilder.path("post/create")
                                                                           .build())
                                              .bodyValue(createPostArg)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        PostDto postDto = response.get(0);
        Post createdPost = postService.get(postDto.getId());

        Assertions.assertEquals(postDto.getName(), createdPost.getName());
        Assertions.assertEquals(postDto.getId(), createdPost.getId());
    }

    @Test
    void update() {
        // Arrange
        UUID updatedId = posts.get(0).getId();
        UpdatePostArgumentDto updatePostArg = UpdatePostArgumentDto.builder()
                                                                   .name("Mobile")
                                                                   .build();

        // Act
        List<PostDto> response = webTestClient.put()
                                              .uri(uriBuilder -> uriBuilder.path("post/update/" + updatedId)
                                                                           .build())
                                              .bodyValue(updatePostArg)
                                              .exchange()

                                              // Assert
                                              .expectStatus()
                                              .isOk()
                                              .expectBodyList(PostDto.class)
                                              .returnResult()
                                              .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        PostDto postDto = response.get(0);
        Post updatedPost = postService.get(updatedId);

        Assertions.assertEquals(postDto.getName(), updatedPost.getName());
        Assertions.assertEquals(postDto.getId(), updatedPost.getId());
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

        Assertions.assertThrows(NotFoundException.class, () -> postService.get(deletedPost.getId()));
    }
}
