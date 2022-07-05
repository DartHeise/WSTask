package com.ws.task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.controller.employee.mapper.EmployeeMapperImpl;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.util.ReadValueAction;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(SoftAssertionsExtension.class)
public class EmployeeServiceTest {

    private EmployeeService employeeService;

    private List<Employee> employees;

    private final EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    private final ReadValueAction readValueAction = new ReadValueAction();

    @BeforeEach
    public void setUp() throws IOException {
        employees = readValueAction.execute
                ("jsons\\service\\employee\\employees.json", new TypeReference<>() {});

        employeeService = new EmployeeService(new HashMap<>(), employeeMapper);
        employeeService.addEmployees(employees);
    }

    @ParameterizedTest
    @MethodSource
    public void getAllOrdered(String path, String name, UUID postId) throws Exception {
        // Arrange
        List<Employee> expected = readValueAction.execute
                (path, new TypeReference<>() {});

        // Act
        List<Employee> actual = employeeService.getAllOrdered
                (new SearchingParameters(name, postId));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getEmployeeById() throws IOException {
        // Arrange
        Employee expected = readValueAction.execute
                ("jsons\\service\\employee\\employee_for_get_employee_by_id_test.json", Employee.class);

        UUID employeeId = employees.get(0).getId();

        // Act
        Employee actual = employeeService.get(employeeId);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getNotExistingEmployeeById() throws IOException {
        // Arrange
        Employee employee = readValueAction.execute
                ("jsons\\service\\employee\\employee_for_get_not_existing_employee_by_id_test.json",
                                                                                          Employee.class);

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows
                (NotFoundException.class, () -> employeeService.get(employee.getId()));
        String expectedMessage = "Employee not found";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void createEmployee(SoftAssertions softAssertions) throws IOException {
        // Arrange
        EmployeeArgument employeeArgument = readValueAction.execute
                ("jsons\\service\\employee\\employee_for_create.json", CreateEmployeeArgument.class);

        // Act
        Employee actual = employeeService.create(employeeArgument);

        // Assert
        Employee expected = employeeMapper.toEmployee(employeeArgument, actual.getId());
        softAssertions.assertThat(expected).isEqualTo(actual);

        softAssertions.assertThat
                (employeeService.getAllOrdered(new SearchingParameters()).size()).isEqualTo(3);
    }

    @Test
    public void updateEmployee(SoftAssertions softAssertions) throws IOException {
        // Arrange
        EmployeeArgument employeeArgument = readValueAction.execute
                ("jsons\\service\\employee\\employee_for_update.json", UpdateEmployeeArgument.class);

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
    public void deleteEmployee(SoftAssertions softAssertions) {
        // Arrange
        Employee deletedEmployee = employees.get(0);
        UUID deletedEmployeeId = deletedEmployee.getId();

        // Act
        employeeService.delete(deletedEmployeeId);

        // Assert
        List<Employee> resultEmployees = employeeService.getAllOrdered(new SearchingParameters());

        softAssertions.assertThat(resultEmployees.size()).isEqualTo(1);
        softAssertions.assertThat(resultEmployees.contains(deletedEmployee)).isEqualTo(false);
    }

    private static Stream<Arguments> getAllOrdered() {
        return Stream.of(
                Arguments.of("jsons\\service\\employee\\sorted_employees.json",
                                                                   null, null),

                Arguments.of("jsons\\service\\employee\\employees_with_backend_id.json",
                         null, UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")),

                Arguments.of("jsons\\service\\employee\\employees_with_first_name_ivan.json",
                                                                               "Ivan", null),

                Arguments.of("jsons\\service\\employee\\employees_with_last_name_ivanov.json",
                                                                              "Ivanov", null),

                Arguments.of("jsons\\service\\employee\\employees_with_first_name_denis_and_backend_id.json",
                                           "Denis", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")),

                Arguments.of("jsons\\service\\employee\\employees_with_last_name_losev_and_backend_id.json",
                                           "Losev", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"))
        );
    }
}

