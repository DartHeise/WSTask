package com.ws.task.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.jupiter.tools.spring.test.postgres.annotation.meta.EnablePostgresIntegrationTest;
import com.ws.task.controller.post.dto.CreatePostDto;
import com.ws.task.controller.post.dto.PostDto;
import com.ws.task.controller.post.dto.UpdatePostDto;
import com.ws.task.logging.ApiRequestLoggingAspect;
import com.ws.task.service.postService.PostService;
import com.ws.task.util.LogAppender;
import com.ws.task.util.ReadValueAction;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient
@EnablePostgresIntegrationTest
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIT {

    private final ReadValueAction readValueAction = new ReadValueAction();
    private final UUID postId = UUID.fromString("e0f075a2-efa1-11ec-8ea0-0242ac120002");
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private PostService postService;
    private LogAppender apiRequestLogAppender;
    private List<PostDto> expectedPostDtos;

    @BeforeEach
    private void setUp() throws IOException {
        apiRequestLogAppender = new LogAppender();
        apiRequestLogAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(ApiRequestLoggingAspect.class);
        logger.addAppender(apiRequestLogAppender);

        expectedPostDtos = readValueAction.execute
                                                  ("jsons\\controller\\post\\expected\\post_dtos.json",
                                                   new TypeReference<>() {});
    }

    @Test
    @DataSet(value = "jsons\\controller\\post\\datasets\\get_posts.json", cleanAfter = true, cleanBefore = true)
    void get() throws IOException {
        // Act
        PostDto response = webTestClient.get()
                                        .uri("post/{id}", postId)
                                        .exchange()

                                        // Assert
                                        .expectStatus()
                                        .isOk()
                                        .expectBody(PostDto.class)
                                        .returnResult()
                                        .getResponseBody();

        PostDto expectedPostDto = readValueAction.execute
                                                         ("jsons\\controller\\post\\expected\\get_post_dto.json",
                                                          PostDto.class);

        Assertions.assertEquals(expectedPostDto, response);

        assertApiRequestLog("PostDto com.ws.task.controller.post.PostController.getPost(UUID)",
                            String.format("[%s]", postId));
    }

    @Test
    @DataSet(value = "jsons\\controller\\post\\datasets\\get_all_posts.json", cleanAfter = true, cleanBefore = true)
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

        assertApiRequestLog("List com.ws.task.controller.post.PostController.getAllPosts()",
                            "[]");
    }

    @Test
    @ExpectedDataSet(value = "jsons\\controller\\post\\datasets\\create_expected_posts.json")
    void create() throws IOException {
        // Arrange
        CreatePostDto createPostDto = readValueAction.execute
                                                             ("jsons\\controller\\post\\create_post_dto.json",
                                                              CreatePostDto.class);

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

        PostDto expectedPostDto = readValueAction.execute
                                                         ("jsons\\controller\\post\\expected\\create_post_dto.json",
                                                          PostDto.class);
        expectedPostDto.setId(response.getId());

        Assertions.assertEquals(expectedPostDto, response);

        assertApiRequestLog("PostDto com.ws.task.controller.post.PostController.createPost(CreatePostDto)",
                            String.format("[%s]", createPostDto));
    }

    @Test
    @DataSet(value = "jsons\\controller\\post\\datasets\\update_posts.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\post\\datasets\\update_expected_posts.json")
    void update() throws IOException {
        // Arrange
        UpdatePostDto updatePostDto = readValueAction.execute
                                                             ("jsons\\controller\\post\\update_post_dto.json",
                                                              UpdatePostDto.class);

        // Act
        PostDto response = webTestClient.put()
                                        .uri("post/{id}/update", postId)
                                        .bodyValue(updatePostDto)
                                        .exchange()

                                        // Assert
                                        .expectStatus()
                                        .isOk()
                                        .expectBody(PostDto.class)
                                        .returnResult()
                                        .getResponseBody();

        PostDto expectedPostDto = readValueAction.execute
                                                         ("jsons\\controller\\post\\expected\\update_post_dto.json",
                                                          PostDto.class);

        Assertions.assertEquals(expectedPostDto, response);

        assertApiRequestLog("PostDto com.ws.task.controller.post.PostController.updatePost(UUID,UpdatePostDto)",
                            String.format("[%s, %s]", postId, updatePostDto));
    }

    @Test
    @DataSet(value = "jsons\\controller\\post\\datasets\\delete_posts.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\post\\datasets\\expected_delete_posts.json")
    void delete() {
        // Act
        webTestClient.delete()
                     .uri("post/{id}/delete", postId)
                     .exchange()

                     // Assert
                     .expectStatus()
                     .isOk();

        assertApiRequestLog("void com.ws.task.controller.post.PostController.deletePost(UUID)",
                            String.format("[%s]", postId));
    }

    private void assertApiRequestLog(String arguments, String callMethod) {
        String requestLogMessage = String.format("client IP address: %s; call %s with arguments: %s",
                                                 "127.0.0.1", arguments, callMethod);

        assertThat(apiRequestLogAppender.getLogEvents()).isNotEmpty()
                                                        .anySatisfy(event -> assertThat(event.getMessage())
                                                                .isEqualTo(requestLogMessage));

        apiRequestLogAppender.stop();
    }
}
