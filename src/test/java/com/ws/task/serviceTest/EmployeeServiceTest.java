package com.ws.task.serviceTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.model.employee.mapper.EmployeeMapper;
import com.ws.task.model.employee.mapper.EmployeeMapperImpl;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    private List<Employee> employees;

    @BeforeEach
    public void setUp() throws IOException {
        employeeService = new EmployeeService(new HashMap<>(), new EmployeeMapperImpl());
        employees = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employees.json"),
                                                                                      new TypeReference<>() {});
        employeeService.addEmployees(employees);
    }

    @Test
    public void getAllOrderedWithoutSearchingParameters() throws Exception {
        // Arrange
        List<Employee> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\sortedEmployees.json"),
                                                                                            new TypeReference<>() {});
        List<Employee> actual;

        // Act
        actual = employeeService.getAllOrdered(new SearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithIdOnly() throws Exception {
        // Arrange
        List<Employee> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeesWithBackendId.json"),
                                                                                                   new TypeReference<>() {});
        List<Employee> actual;

        // Act
        actual = employeeService.getAllOrdered
                (new SearchingParameters(null, UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithFirstNameOnly() throws Exception {
        // Arrange
        List<Employee> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeesWithFirstNameIvan.json"),
                                                                                                       new TypeReference<>() {});
        List<Employee> actual;

        // Act
        actual = employeeService.getAllOrdered(new SearchingParameters("Ivan", null));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithLastNameOnly() throws Exception {
        // Arrange
        List<Employee> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeesWithLastNameIvanov.json"),
                                                                                                        new TypeReference<>() {});
        List<Employee> actual;

        // Act
        actual = employeeService.getAllOrdered(new SearchingParameters("Ivanov", null));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithFirstNameAndId() throws Exception {
        // Arrange
        List<Employee> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeesWithFirstNameDenisAndBackendId.json"),
                                                                                                                    new TypeReference<>() {});
        List<Employee> actual;

        // Act
        actual = employeeService.getAllOrdered
                (new SearchingParameters("Denis", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithLastNameAndId() throws Exception {
        // Arrange
        List<Employee> expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\EmployeesWithLastNameLosevAndBackendId.json"),
                                                                                                                   new TypeReference<>() {});
        List<Employee> actual;

        // Act
        actual = employeeService.getAllOrdered
                (new SearchingParameters("Losev", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")));

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getEmployeeById() throws IOException {
        // Arrange
        Employee expected = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeeForGetEmployeeByIdTest.json"),
                                                                                                                     Employee.class);
        Employee actual;

        // Act
        actual = employeeService.get(employees.get(0).getId());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getNotExistingEmployeeById() throws IOException {
        // Arrange
        Employee employee = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeeForGetNotExistingEmployeeByIdTest.json"),
                                                                                                                                Employee.class);
        NotFoundException exception;
        String expectedMessage = "Employee not found";

        // Act & Assert
        exception = Assertions.assertThrows(NotFoundException.class, () -> employeeService.get(employee.getId()));
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void createEmployee() throws IOException {
        // Arrange
        EmployeeMapper employeeMapper = new EmployeeMapperImpl();
        EmployeeArgument employeeArgument = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeeForCreate.json"),
                                                                                          CreateEmployeeArgument.class);
        Employee expected;
        Employee actual;

        // Act
        actual = employeeService.create(employeeArgument);

        // Assert
        expected = employeeMapper.toEmployee(employeeArgument, actual.getId());
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals
                (employeeService.getAllOrdered(new SearchingParameters()).size(), 3);
    }

    @Test
    public void updateEmployee() throws IOException {
        // Arrange
        EmployeeMapper employeeMapper = new EmployeeMapperImpl();
        EmployeeArgument employeeArgument = new ObjectMapper().readValue
                (getClass().getClassLoader().getResource("jsons\\serviceTests\\employee\\employeeForCreate.json"),
                                                                                          UpdateEmployeeArgument.class);
        Employee expected;
        Employee actual;

        UUID updatedId = employees.get(0).getId();

        // Act
        actual = employeeService.update(employeeArgument, updatedId);

        // Assert
        expected = employeeMapper.toEmployee(employeeArgument, updatedId);
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals
                (employeeService.getAllOrdered(new SearchingParameters()).size(), 2);
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
