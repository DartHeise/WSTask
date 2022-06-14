package com.ws.task.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.employee.dto.CreateEmployeeArgumentDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.Post;
import com.ws.task.model.employee.Contacts;
import com.ws.task.model.employee.Employee;
import com.ws.task.model.employee.JobType;
import com.ws.task.service.employeeService.CreateEmployeeArgument;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.postService.CreatePostArgument;
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
public class EmployeeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private  PostService postService;

    private List<Employee> employees;

    @BeforeEach
    private void setUp() throws IOException {
        employeeService.deleteAll();
        CreateEmployeeArgument[] createEmployeeArguments = new ObjectMapper().readValue
                (new File("src\\test\\java\\com\\ws\\task\\resources\\jsons\\controllerTests\\Employees.json"), CreateEmployeeArgument[].class);
        Arrays.stream(createEmployeeArguments).forEach(x -> employeeService.create(x));
        employees = employeeService.getAllOrdered(new SearchingParameters());
    }

    @Test
    void get() {
        Employee employee = employees.get(0);

        List<EmployeeDto> response = webTestClient.get()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/get/" + employee.getId().toString())
                                                                               .build())
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        EmployeeDto employeeDto = response.get(0);

        compareEmployeeDtoToModelEmployee(employeeDto, employee);
    }

    @Test
    void getAll() {
        SearchingParameters searchParams = new SearchingParameters();

        List<EmployeeDto> response = webTestClient.post()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/getAll")
                                                                               .build())
                                                  .bodyValue(searchParams)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 2);

        EmployeeDto firstEmployeeDto = response.get(0);
        compareEmployeeDtoToModelEmployee(firstEmployeeDto, employees.get(0));

        EmployeeDto secondEmployeeDto = response.get(1);
        compareEmployeeDtoToModelEmployee(secondEmployeeDto, employees.get(1));
    }

    @Test
    void post() {
        Post post = postService.create(CreatePostArgument.builder()
                                                         .name("Frontend")
                                                         .build());

        CreateEmployeeArgumentDto createEmployeeArgDto = CreateEmployeeArgumentDto
                .builder()
                .firstName("Artem")
                .lastName("Kornev")
                .description("")
                .characteristics(List.of("shy", "tactful", "resourceful", "reliable"))
                .postId(post.getId())
                .jobType(JobType.PERMANENT)
                .contacts(Contacts.builder()
                        .phone("+16463483212")
                        .email("Kornevenrok@gmail.com")
                        .workEmail("KornevWorker123@bk.ru")
                        .build())
                .build();

        List<EmployeeDto> response = webTestClient.post()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/create")
                                                                               .build())
                                                  .bodyValue(createEmployeeArgDto)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        EmployeeDto createdEmployeeDto = response.get(0);
        compareEmployeeDtoToModelEmployee
                (createdEmployeeDto, employeeService.get(createdEmployeeDto.getId()));
    }

    @Test
    void update() {
        Post post = postService.create(CreatePostArgument.builder()
                .name("Frontend")
                .build());

        CreateEmployeeArgumentDto createEmployeeArgDto = CreateEmployeeArgumentDto
                .builder()
                .firstName("Artem")
                .lastName("Kornev")
                .description("")
                .characteristics(List.of("shy", "tactful", "resourceful", "reliable"))
                .postId(post.getId())
                .jobType(JobType.PERMANENT)
                .contacts(Contacts.builder()
                        .phone("+16463483212")
                        .email("Kornevenrok@gmail.com")
                        .workEmail("KornevWorker123@bk.ru")
                        .build())
                .build();

        UUID updatedId = employees.get(0).getId();

        List<EmployeeDto> response = webTestClient.put()
                                                  .uri(uriBuilder -> uriBuilder.path("employee/update/" + updatedId)
                                                                               .build())
                                                  .bodyValue(createEmployeeArgDto)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(EmployeeDto.class)
                                                  .returnResult()
                                                  .getResponseBody();

        Assertions.assertEquals(response.size(), 1);

        EmployeeDto employeeDto = response.get(0);
        Employee updatedEmployee = employeeService.get(updatedId);

        compareEmployeeDtoToModelEmployee(employeeDto, updatedEmployee);
    }

    @Test
    void delete() {
        Employee deletedEmployee = employees.get(0);

        webTestClient.delete()
                     .uri(uriBuilder -> uriBuilder.path("employee/delete/" + deletedEmployee.getId())
                                                  .build())
                     .exchange()
                     .expectStatus()
                     .isOk();

        Assertions.assertEquals(1, employeeService.getAllOrdered(new SearchingParameters()).size());

        Assertions.assertThrows(NotFoundException.class, () -> employeeService.get(deletedEmployee.getId()));
    }

    private void compareEmployeeDtoToModelEmployee(EmployeeDto employeeDto, Employee employee) {
        Assertions.assertEquals(employeeDto.getFirstName(), employee.getFirstName());
        Assertions.assertEquals(employeeDto.getLastName(), employee.getLastName());
        Assertions.assertEquals(employeeDto.getDescription(), employee.getDescription());
        Assertions.assertEquals(employeeDto.getCharacteristics(), employee.getCharacteristics());
        Assertions.assertEquals(employeeDto.getPost(), employee.getPost());
        Assertions.assertEquals(employeeDto.getId(), employee.getId());
        Assertions.assertEquals(employeeDto.getContacts(), employee.getContacts());
        Assertions.assertEquals(employeeDto.getJobType(), employee.getJobType());
    }
}
