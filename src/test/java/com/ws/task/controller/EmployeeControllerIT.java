package com.ws.task.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class EmployeeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private PostService postService;

    private List<Employee> employees;

    private List<EmployeeDto> expectedEmployeeDtos;

    private SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    private void setUp() throws IOException {
        employees = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\employees.json"),
                                                                                    new TypeReference<>() {});
        expectedEmployeeDtos = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\expected_employee_dtos.json"),
                                                                                                 new TypeReference<>() {});
    }

    @Test
    void get() {
        // Arrange
        UUID employeeId = employees.get(0).getId();
        Employee employee = employees.get(0);

        when(employeeService.get(employeeId)).thenReturn(employee);

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
    void getAll() {
        // Arrange
        when(employeeService.getAllOrdered(new SearchingParameters())).thenReturn(employees);

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
        CreateEmployeeDto createEmployeeDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\create_employee_dto.json"),
                                                                                                 CreateEmployeeDto.class);
        EmployeeArgument createEmployeeArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\create_employee_argument.json"),
                                                                                               CreateEmployeeArgument.class);
        Employee createdEmployee = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\created_employee.json"),
                                                                                                     Employee.class);
        Post mobilePost = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\create_mobile_post.json"),
                                                                                                           Post.class);
        UUID mobileId = createEmployeeDto.getPostId();

        when(employeeService.create(createEmployeeArgument)).thenReturn(createdEmployee);
        when(postService.get(mobileId)).thenReturn(mobilePost);

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

        EmployeeDto expectedEmployeeDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\create_expected.json"),
                                                                                                 EmployeeDto.class);

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    void update() throws IOException {
        // Arrange
        UUID updatedId = employees.get(0).getId();

        UpdateEmployeeDto updateEmployeeDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\update_employee_dto.json"),
                                                                                               UpdateEmployeeDto.class);
        UpdateEmployeeArgument updateEmployeeArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\update_employee_argument.json"),
                                                                                               UpdateEmployeeArgument.class);
        Employee updatedEmployee = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\updated_employee.json"),
                                                                                                     Employee.class);
        Post mobilePost = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\update_mobile_post.json"),
                                                                                                           Post.class);
        UUID mobileId = updateEmployeeDto.getPostId();

        when(employeeService.update(updateEmployeeArgument, updatedId)).thenReturn(updatedEmployee);
        when(postService.get(mobileId)).thenReturn(mobilePost);

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

        EmployeeDto expectedEmployeeDto = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\controller\\employee\\update_expected.json"),
                                                                                                 EmployeeDto.class);

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    void delete() {
        // Arrange
        UUID deletedEmployeeId = employees.get(0).getId();

        // Act
        webTestClient.delete()
                     .uri("employee/{id}/delete", deletedEmployeeId)
                     .exchange()
                     // Assert
                     .expectStatus()
                     .isOk();
    }
}
