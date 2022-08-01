package com.ws.task.controller;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.jupiter.tools.spring.test.postgres.annotation.meta.EnablePostgresIntegrationTest;
import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.dto.UpdateEmployeeDto;
import com.ws.task.logging.ApiRequestLoggingAspect;
import com.ws.task.logging.UpdateEmployeeLoggingAspect;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.postService.PostService;
import com.ws.task.util.LogAppender;
import com.ws.task.util.ReadValueAction;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    private LogAppender apiRequestLogAppender;

    private LogAppender updateEmployeeLogAppender;

    private final ReadValueAction readValueAction = new ReadValueAction();

    private final UUID employeeId = UUID.fromString("8ee05ef8-eed9-11ec-8ea0-0242ac120002");

    @BeforeEach
    private void setUp() {
        apiRequestLogAppender = new LogAppender();
        apiRequestLogAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(ApiRequestLoggingAspect.class);
        logger.addAppender(apiRequestLogAppender);
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\get_employees.json", cleanAfter = true, cleanBefore = true)
    void get() throws IOException {
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

        EmployeeDto expectedEmployeeDto = readValueAction.execute
                                                                 ("jsons\\controller\\employee\\expected\\get_employee_dto.json",
                                                                  EmployeeDto.class);

        Assertions.assertEquals(expectedEmployeeDto, response);

        assertApiRequestLog("EmployeeDto com.ws.task.controller.employee.EmployeeController.getEmployee(UUID)",
                            String.format("[%s]", employeeId));
    }

    @MethodSource
    @ParameterizedTest
    @DataSet(value = "jsons\\controller\\employee\\datasets\\get_all_employees.json", cleanAfter = true, cleanBefore = true)
    void getAllOrderedAndFilteredByNameAndPostId(String path, String name, UUID postId) throws IOException {
        // Act
        List<EmployeeDto> response = webTestClient.get()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/list")
                                                                               .queryParam("name", name)
                                                                               .queryParam("postId", postId)
                                                                               .build())
                                                  .exchange()

                                                  // Assert
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        List<EmployeeDto> expected = readValueAction.execute
                                                            (path, new TypeReference<>() {});

        Assertions.assertEquals(expected, response);

        assertApiRequestLog("List com.ws.task.controller.employee.EmployeeController.getAllEmployees(SearchingParameters,Sort)",
                            String.format("[SearchingParameters(name=%s, postId=%s), lastName: ASC,firstName: ASC]", name, postId));
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
                                                                 ("jsons\\controller\\employee\\expected\\create_employee_dto.json",
                                                                  EmployeeDto.class);
        expectedEmployeeDto.setId(response.getId());

        Assertions.assertEquals(expectedEmployeeDto, response);

        assertApiRequestLog("EmployeeDto com.ws.task.controller.employee.EmployeeController.createEmployee(CreateEmployeeDto)",
                            String.format("[%s]", createEmployeeDto));
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\update_employees.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\employee\\datasets\\update_expected_employees.json")
    void update() throws IOException {
        // Arrange
        updateEmployeeLogAppender = new LogAppender();
        updateEmployeeLogAppender.start();
        Logger logger = (Logger) LoggerFactory.getLogger(UpdateEmployeeLoggingAspect.class);
        logger.addAppender(updateEmployeeLogAppender);

        UpdateEmployeeDto updateEmployeeDto = readValueAction.execute
                                                                     ("jsons\\controller\\employee\\update_employee_dto.json",
                                                                      UpdateEmployeeDto.class);

        // Act
        EmployeeDto response = webTestClient.put()
                                            .uri("employee/{id}/update", employeeId)
                                            .bodyValue(updateEmployeeDto)
                                            .exchange()

                                            // Assert
                                            .expectStatus()
                                            .isOk()
                                            .expectBody(EmployeeDto.class)
                                            .returnResult()
                                            .getResponseBody();

        EmployeeDto expectedEmployeeDto = readValueAction.execute
                                                                 ("jsons\\controller\\employee\\expected\\update_employee_dto.json",
                                                                  EmployeeDto.class);

        Assertions.assertEquals(expectedEmployeeDto, response);

        assertApiRequestLog("EmployeeDto com.ws.task.controller.employee.EmployeeController.updateEmployee(UUID,UpdateEmployeeDto)",
                            String.format("[%s, %s]", employeeId, updateEmployeeDto));
        assertUpdateEmployeeLog();
    }

    @Test
    @DataSet(value = "jsons\\controller\\employee\\datasets\\delete_employees.json", cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "jsons\\controller\\employee\\datasets\\delete_expected_employees.json")
    void delete() {
        // Act
        webTestClient.delete()
                     .uri("employee/{id}/delete", employeeId)
                     .exchange()

                     // Assert
                     .expectStatus()
                     .isOk();

        assertApiRequestLog("void com.ws.task.controller.employee.EmployeeController.deleteEmployee(UUID)",
                            String.format("[%s]", employeeId));
    }

    private void assertApiRequestLog(String arguments, String callMethod) {
        String requestLogMessage = String.format("client IP address: %s; call %s with arguments: %s",
                                                 "127.0.0.1", arguments, callMethod);

        assertThat(apiRequestLogAppender.getLogEvents()).isNotEmpty()
                                                        .anySatisfy(event -> assertThat(event.getMessage())
                                                                .isEqualTo(requestLogMessage));

        apiRequestLogAppender.stop();
    }

    private void assertUpdateEmployeeLog() {
        String updatedEmployeeIdLog = String.format("Updating employee with id: %s", employeeId);

        assertThat(updateEmployeeLogAppender.getLogEvents()).isNotEmpty()
                                                            .anySatisfy(event -> assertThat(event.getMessage())
                                                                    .isEqualTo(getUpdatedEmployeeFieldsLog()))
                                                            .anySatisfy(event -> assertThat(event.getMessage())
                                                                    .isEqualTo(updatedEmployeeIdLog));

        updateEmployeeLogAppender.stop();
    }

    private static Stream<Arguments> getAllOrderedAndFilteredByNameAndPostId() {
        return Stream.of(
                Arguments.of("jsons\\controller\\employee\\expected\\sorted_employees.json",
                             "", null),

                Arguments.of("jsons\\controller\\employee\\expected\\employees_with_backend_id.json",
                             "", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")),

                Arguments.of("jsons\\controller\\employee\\expected\\employees_with_first_name_ivan.json",
                             "Ivan", null),

                Arguments.of("jsons\\controller\\employee\\expected\\employees_with_last_name_ivanov.json",
                             "Ivanov", null),

                Arguments.of("jsons\\controller\\employee\\expected\\employees_with_first_name_denis_and_backend_id.json",
                             "Denis", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")),

                Arguments.of("jsons\\controller\\employee\\expected\\employees_with_last_name_losev_and_backend_id.json",
                             "Losev", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"))
                        );
    }

    private String getUpdatedEmployeeFieldsLog() {
        return "Updating fields... " +
               "firstName: [Ivan] -> [Artem] " +
               "lastName: [Ivanov] -> [Kornev] " +
               "description: [Lorem ipsum dolor sit amet] -> [consectetur adipiscing elit] " +
               "characteristics: [[active, cynical, hard-working, enthusiastic]] -> " +
               "[[shy, tactful, resourceful, reliable]] " +
               "contacts: [Contacts(phone=+16463211930, email=Ivanov14@mail.ru, workEmail=IvanovWorker321@bk.ru)] -> " +
               "[Contacts(phone=+16463483212, email=Kornevenrok@gmail.com, workEmail=KornevWorker123@bk.ru)] " +
               "jobType: [TEMPORARY] -> [PERMANENT] " +
               "post: [Post(id=762d15a5-3bc9-43ef-ae96-02a680a557d0, name=Frontend)] -> " +
               "[Post(id=854ef89d-6c27-4635-926d-894d76a81707, name=Backend)]";
    }
}
