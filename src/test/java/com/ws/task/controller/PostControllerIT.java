package com.ws.task.controller;

import com.ws.task.controller.post.dto.CreatePostDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostDto;
import com.ws.task.controller.post.mapper.PostMapper;
import com.ws.task.controller.post.mapper.PostMapperImpl;
import com.ws.task.model.post.Post;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.util.ReadValueAction;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AutoConfigureWebTestClient
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PostService postService;

    private List<Post> posts;

    private List<PostDto> expectedPostDtos;

    private final PostMapper postMapper = new PostMapperImpl();

    private final ReadValueAction readValueAction = new ReadValueAction();

    @BeforeEach
    private void setUp() throws IOException {
        postService.deleteAll();

        CreatePostArgument[] createPostArguments = readValueAction.execute
                ("jsons\\controller\\post\\create_post_arguments.json", CreatePostArgument[].class);

        Arrays.stream(createPostArguments).forEach(x -> postService.create(x));

        posts = postService.getAll();

        expectedPostDtos = posts.stream()
                                .map(postMapper::toPostDto)
                                .collect(Collectors.toList());
    }

    @Test
    void get() {
        // Arrange
        Post backendPost = posts.get(0);
        UUID backendPostId = backendPost.getId();

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
    void getAll(SoftAssertions softAssertions) {
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
        CreatePostDto createPostDto = readValueAction.execute
                ("jsons\\controller\\post\\create_post_dto.json", CreatePostDto.class);

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

        UUID createdPostId = response.getId();
        Post createdPost = postService.get(createdPostId);
        PostDto expectedPostDto = postMapper.toPostDto(createdPost);

        Assertions.assertEquals(expectedPostDto, response);
    }

    @Test
    void update() throws IOException {
        // Arrange
        UUID updatedId = posts.get(0).getId();
        UpdatePostDto updatePostDto = readValueAction.execute
                ("jsons\\controller\\post\\update_post_dto.json", UpdatePostDto.class);

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

        Post updatedPost = postService.get(updatedId);
        PostDto expectedPostDto = postMapper.toPostDto(updatedPost);

        Assertions.assertEquals(expectedPostDto, response);
    }

    @Test
    void delete(SoftAssertions softAssertions) {
        // Arrange
        Post deletedPost = posts.get(0);
        UUID deletedPostId = deletedPost.getId();

        // Act
        webTestClient.delete()
                     .uri("post/{id}/delete", deletedPostId)
                     .exchange()

                     // Assert
                     .expectStatus()
                     .isOk();

        List<Post> resultPosts = postService.getAll();

        softAssertions.assertThat(resultPosts.size()).isEqualTo(2);
        softAssertions.assertThat(resultPosts.contains(deletedPost)).isEqualTo(false);
    }
}
