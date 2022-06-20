package com.ws.task.controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.mapper.EmployeeControllerMapper;
import com.ws.task.model.post.Post;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
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
public class EmployeeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PostService postService;

    @Autowired
    private EmployeeControllerMapper employeeControllerMapper;

    private List<Employee> employees;

    private List<EmployeeDto> expectedEmployeeDtos;

    @BeforeEach
    private void setUp() throws IOException {
        employeeService.deleteAll();
        employees = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\employee\\employees.json"),
                                                                                         new TypeReference<>() {});
        expectedEmployeeDtos = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\employee\\expectedEmployeeDtos.json"),
                                                                                                    new TypeReference<>() {});
        employeeService.addEmployees(employees);
    }

    @Test
    void get() {
        // Arrange
        Employee employee = employees.get(0);
        EmployeeDto expectedEmployeeDto = expectedEmployeeDtos.get(0);

        // Act
        List<EmployeeDto> response = webTestClient.get()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/" + employee.getId().toString())
                                                                               .build())
                                                  .exchange()

                                                  // Assert
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        EmployeeDto actualEmployeeDto = response.get(0);

        Assertions.assertEquals(expectedEmployeeDto, actualEmployeeDto);
    }

    @Test
    void getAll() {
        // Arrange
        SearchingParameters searchParams = new SearchingParameters();

        // Act
        List<EmployeeDto> response = webTestClient.get()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/getAll")
                                                                               .queryParam("name",
                                                                                                 searchParams.getName())
                                                                               .queryParam("postId",
                                                                                                 searchParams.getPostId())
                                                                               .build())
                                                  .exchange()

                                                  // Assert
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 2);

        EmployeeDto firstEmployeeDto = response.get(0);
        Assertions.assertEquals(expectedEmployeeDtos.get(0), firstEmployeeDto);

        EmployeeDto secondEmployeeDto = response.get(1);
        Assertions.assertEquals(expectedEmployeeDtos.get(1), secondEmployeeDto);
    }

    @Test
    void create() throws IOException {
        // Arrange
        List<Post> posts = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\employee\\postForCreate.json"),
                                                                                             new TypeReference<>() {});
        postService.addPosts(posts);
        CreateEmployeeDto createEmployeeDto = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\employee\\employeeForCreate.json"),
                                                                                                 new TypeReference<>() {});

        // Act
        List<EmployeeDto> response = webTestClient.post()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/create")
                                                                               .build())
                                                  .bodyValue(createEmployeeDto)
                                                  .exchange()

                                                  // Assert
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        EmployeeDto actualEmployeeDto = response.get(0);
        EmployeeDto createdEmployee = employeeControllerMapper.toEmployeeDto
                (employeeService.get(actualEmployeeDto.getId()), actualEmployeeDto.getPostId());

        Assertions.assertEquals(createdEmployee, actualEmployeeDto);
    }

    @Test
    void update() throws IOException {
        // Arrange
        UUID updatedId = employees.get(0).getId();
        List<Post> posts = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\employee\\postForUpdate.json"),
                                                                                             new TypeReference<>() {});
        postService.addPosts(posts);
        CreateEmployeeDto updateEmployeeDto = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\controllerTests\\employee\\employeeForUpdate.json"),
                                                                                                 new TypeReference<>() {});

        // Act
        List<EmployeeDto> response = webTestClient.put()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/update/" + updatedId)
                                                                               .build())
                                                  .bodyValue(updateEmployeeDto)
                                                  .exchange()

                                                  // Assert
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        EmployeeDto actualEmployeeDto = response.get(0);
        EmployeeDto updatedEmployee = employeeControllerMapper.toEmployeeDto
                (employeeService.get(updatedId), actualEmployeeDto.getPostId());

        Assertions.assertEquals(updatedEmployee, actualEmployeeDto);
    }

    @Test
    void delete() {
        // Arrange
        Employee deletedEmployee = employees.get(0);

        // Act
        webTestClient.delete()
                     .uri(uriBuilder -> uriBuilder.path("employee/delete/" + deletedEmployee.getId())
                                                  .build())
                     .exchange()
                     // Assert
                     .expectStatus()
                     .isOk();

        Assertions.assertEquals(1, employeeService.getAllOrdered(new SearchingParameters()).size());
    }
}
