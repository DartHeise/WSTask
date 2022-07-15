package com.ws.task.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.jupiter.tools.spring.test.postgres.annotation.meta.EnablePostgresIntegrationTest;
import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.dto.UpdateEmployeeDto;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.postService.PostService;
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
import java.util.List;
import java.util.UUID;

@AutoConfigureWebTestClient
@EnablePostgresIntegrationTest
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PostService postService;

    private List<EmployeeDto> expectedEmployeeDtos;

    private final ReadValueAction readValueAction = new ReadValueAction();

    @BeforeEach
    private void setUp() throws IOException {
        expectedEmployeeDtos = readValueAction.execute
                                                      ("jsons\\controller\\employee\\expected_employee_dtos.json",
                                                       new TypeReference<>() {});
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\get_employees.json", cleanAfter = true, cleanBefore = true)
    void get() {
        // Arrange
        EmployeeDto expectedEmployeeDto = expectedEmployeeDtos.get(0);
        UUID employeeId = expectedEmployeeDto.getId();

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

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\get_all_employees.json", cleanAfter = true, cleanBefore = true)
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
    @DataSet(value = "jsons\\controller\\employee\\datasets\\create_employees.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\employee\\datasets\\create_expected_employees.json")
    void create() throws IOException {
        // Arrange
        CreateEmployeeDto createEmployeeDto = readValueAction.execute
                ("jsons\\controller\\employee\\create_employee_dto.json",
                 CreateEmployeeDto.class);

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
                                                                 ("jsons\\controller\\employee\\create_expected.json",
                                                                  EmployeeDto.class);
        expectedEmployeeDto.setId(response.getId());

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\update_employees.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\employee\\datasets\\update_expected_employees.json")
    void update() throws IOException {
        // Arrange
        UUID updatedId = UUID.fromString("8ee05ef8-eed9-11ec-8ea0-0242ac120002");
        UpdateEmployeeDto updateEmployeeDto = readValueAction.execute
                                                                     ("jsons\\controller\\employee\\update_employee_dto.json",
                                                                      UpdateEmployeeDto.class);

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
                                                                 ("jsons\\controller\\employee\\update_expected.json",
                                                                  EmployeeDto.class);

        Assertions.assertEquals(expectedEmployeeDto, response);
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\delete_employees.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\employee\\datasets\\delete_expected_employees.json")
    void delete() {
        // Arrange
        EmployeeDto deletedEmployee = expectedEmployeeDtos.get(0);
        UUID deletedEmployeeId = deletedEmployee.getId();

        // Act
        webTestClient.delete()
                     .uri("employee/{id}/delete", deletedEmployeeId)
                     .exchange()
                     // Assert
                     .expectStatus()
                     .isOk();
    }
}
