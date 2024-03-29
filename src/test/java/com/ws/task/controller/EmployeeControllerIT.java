package com.ws.task.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.dto.UpdateEmployeeDto;
import com.ws.task.model.employee.Employee;
import com.ws.task.model.post.Post;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.service.postService.PostService;
import com.ws.task.service.postService.arguments.CreatePostArgument;
import com.ws.task.service.postService.arguments.PostArgument;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PostService postService;

    private List<Employee> employees;

    private List<Post> posts;

    private List<EmployeeDto> expectedEmployeeDtos;

    private final ReadValueAction readValueAction = new ReadValueAction();

    @BeforeEach
    private void setUp() throws IOException {
        employeeService.deleteAll();
        postService.deleteAll();

        posts = readValueAction.execute
                ("jsons\\controller\\employee\\posts.json", new TypeReference<>() {});

        employees = readValueAction.execute
                ("jsons\\controller\\employee\\employees.json", new TypeReference<>() {});

        expectedEmployeeDtos = readValueAction.execute
                ("jsons\\controller\\employee\\expected_employee_dtos.json", new TypeReference<>() {});

        postService.addPosts(posts);
        employeeService.addEmployees(employees);
    }

    @Test
    void get() {
        // Arrange
        Employee employee = employees.get(0);
        UUID employeeId = employee.getId();

        // Act
        EmployeeDto response = webTestClient.get()
                .uri("employee/{id}", employeeId)
                .exchange()

                // Assert
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDto.class)
                .returnResult()
                .getResponseBody();

        EmployeeDto expectedEmployeeDto = expectedEmployeeDtos.get(0);

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    void getAll(SoftAssertions softAssertions) {
        // Act
        List<EmployeeDto> response = webTestClient.get()
                .uri("employee/getAll")
                .exchange()

                // Assert
                .expectStatus()
                .isOk()
                .expectBodyList(EmployeeDto.class)
                .returnResult()
                .getResponseBody();

        softAssertions.assertThat(response.size()).isEqualTo(2);

        EmployeeDto firstEmployeeDto = response.get(0);
        softAssertions.assertThat(expectedEmployeeDtos.get(0)).isEqualTo(firstEmployeeDto);

        EmployeeDto secondEmployeeDto = response.get(1);
        softAssertions.assertThat(expectedEmployeeDtos.get(1)).isEqualTo(secondEmployeeDto);
    }

    @Test
    void create() throws IOException {
        // Arrange
        UUID backendId = posts.get(0).getId();
        CreateEmployeeDto createEmployeeDto = readValueAction.execute
                ("jsons\\controller\\employee\\create_employee_dto.json", CreateEmployeeDto.class);

        // Act
        EmployeeDto response = webTestClient.post()
                .uri("employee/create")
                .bodyValue(createEmployeeDto)
                .exchange()

                // Assert
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDto.class)
                .returnResult()
                .getResponseBody();

        EmployeeDto expectedEmployeeDto = readValueAction.execute
                ("jsons\\controller\\employee\\create_expected.json",  EmployeeDto.class);
        expectedEmployeeDto.setId(response.getId());

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    void update() throws IOException {
        // Arrange
        UUID updatedId = employees.get(0).getId();

        UpdateEmployeeDto updateEmployeeDto = readValueAction.execute
                ("jsons\\controller\\employee\\update_employee_dto.json", UpdateEmployeeDto.class);

        // Act
        EmployeeDto response = webTestClient.put()
                .uri("employee/{id}/update", updatedId)
                .bodyValue(updateEmployeeDto)
                .exchange()

                // Assert
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDto.class)
                .returnResult()
                .getResponseBody();

        EmployeeDto expectedEmployeeDto = readValueAction.execute
                ("jsons\\controller\\employee\\update_expected.json", EmployeeDto.class);

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    void delete(SoftAssertions softAssertions) {
        // Arrange
        Employee deletedEmployee = employees.get(0);
        UUID deletedEmployeeId = deletedEmployee.getId();

        // Act
        webTestClient.delete()
                .uri("employee/{id}/delete", deletedEmployeeId)
                .exchange()
                // Assert
                .expectStatus()
                .isOk();

        List<Employee> resultPosts = employeeService.getAllOrdered(new SearchingParameters());

        softAssertions.assertThat(resultPosts.size()).isEqualTo(1);
        softAssertions.assertThat(resultPosts.contains(deletedEmployee)).isEqualTo(false);
    }
}
