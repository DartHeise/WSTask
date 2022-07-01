package com.ws.task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.controller.employee.mapper.EmployeeMapperImpl;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    private List<Employee> employees;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SoftAssertions softAssertions = new SoftAssertions();

    private EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    @BeforeEach
    public void setUp() throws IOException {
        employeeService = new EmployeeService(new HashMap<>(), employeeMapper);
        employees = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employees.json"),
                                                                                 new TypeReference<>() {});
        employeeService.addEmployees(employees);
    }

    @Test
    public void getAllOrderedWithoutSearchingParameters() throws Exception {
        // Arrange
        List<Employee> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\sorted_employees.json"),
                                                                                        new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered(new SearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"854ef89d-6c27-4635-926d-894d76a81707"})
    public void getAllOrderedWithIdOnly(String postId) throws Exception {
        // Arrange
        List<Employee> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employees_with_backend_id.json"),
                                                                                                 new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered
                (new SearchingParameters(null, UUID.fromString(postId)));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ivan"})
    public void getAllOrderedWithFirstNameOnly(String name) throws Exception {
        // Arrange
        List<Employee> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employees_with_first_name_ivan.json"),
                                                                                                      new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered(new SearchingParameters(name, null));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ivanov"})
    public void getAllOrderedWithLastNameOnly(String name) throws Exception {
        // Arrange
        List<Employee> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employees_with_last_name_ivanov.json"),
                                                                                                       new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered(new SearchingParameters(name, null));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"Denis, 854ef89d-6c27-4635-926d-894d76a81707"})
    public void getAllOrderedWithFirstNameAndId(String name, String postId) throws Exception {
        // Arrange
        List<Employee> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employees_with_first_name_denis_and_backend_id.json"),
                                                                                                                      new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered
                (new SearchingParameters(name, UUID.fromString(postId)));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"Losev, 854ef89d-6c27-4635-926d-894d76a81707"})
    public void getAllOrderedWithLastNameAndId(String name, String postId) throws Exception {
        // Arrange
        List<Employee> expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employees_with_last_name_losev_and_backend_id.json"),
                                                                                                                     new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered
                (new SearchingParameters(name, UUID.fromString(postId)));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getEmployeeById() throws IOException {
        // Arrange
        Employee expected = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employee_for_get_employee_by_id_test.json"),
                                                                                                                      Employee.class);
        UUID employeeId = employees.get(0).getId();

        // Act
        Employee actual = employeeService.get(employeeId);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getNotExistingEmployeeById() throws IOException {
        // Arrange
        Employee employee = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employee_for_get_not_existing_employee_by_id_test.json"),
                                                                                                                                   Employee.class);

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows
                (NotFoundException.class, () -> employeeService.get(employee.getId()));
        String expectedMessage = "Employee not found";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void createEmployee() throws IOException {
        // Arrange
        EmployeeArgument employeeArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employee_for_create.json"),
                                                                                       CreateEmployeeArgument.class);

        // Act
        Employee actual = employeeService.create(employeeArgument);

        // Assert
        Employee expected = employeeMapper.toEmployee(employeeArgument, actual.getId());
        softAssertions.assertThat(expected).isEqualTo(actual);

        softAssertions.assertThat
                (employeeService.getAllOrdered(new SearchingParameters()).size()).isEqualTo(3);
    }

    @Test
    public void updateEmployee() throws IOException {
        // Arrange
        EmployeeArgument employeeArgument = objectMapper.readValue
                (getClass().getClassLoader().getResource("jsons\\service\\employee\\employee_for_update.json"),
                                                                                       UpdateEmployeeArgument.class);
        UUID updatedId = employees.get(0).getId();

        // Act
        Employee actual = employeeService.update(employeeArgument, updatedId);

        // Assert
        Employee expected = employeeMapper.toEmployee(employeeArgument, updatedId);
        softAssertions.assertThat(expected).isEqualTo(actual);

        softAssertions.assertThat
                (employeeService.getAllOrdered(new SearchingParameters()).size()).isEqualTo(2);
    }

    @Test
    public void deleteEmployee() {
        // Arrange
        UUID deletedEmployeeId = employees.get(0).getId();

        // Act
        employeeService.delete(deletedEmployeeId);

        // Assert
        Assertions.assertEquals
                (employeeService.getAllOrdered(new SearchingParameters()).size(), 1);
    }
}

