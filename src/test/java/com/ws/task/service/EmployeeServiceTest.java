package com.ws.task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.querydsl.core.types.Predicate;
import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.repository.EmployeeRepository;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.util.ReadValueAction;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(SoftAssertionsExtension.class)
public class EmployeeServiceTest {

    private final EmployeeRepository employeeRepository = mock(EmployeeRepository.class);

    private final EmployeeMapper employeeMapper = mock(EmployeeMapper.class);

    private final EmployeeService employeeService = new EmployeeService
            (employeeMapper, employeeRepository);

    private final ReadValueAction readValueAction = new ReadValueAction();

    @MethodSource
    @ParameterizedTest
    public void getAllOrdered(String path, String name, UUID postId) throws Exception {
        // Arrange
        List<Employee> expected = readValueAction.execute
                                                         (path, new TypeReference<>() {});

        when(employeeRepository.findAll((Predicate) any(), any(), any())).thenReturn(expected);

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
                                                   ("jsons\\service\\employee\\employee_for_get_employee_by_id_test.json",
                                                    Employee.class);

        UUID employeeId = expected.getId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(expected));

        // Act
        Employee actual = employeeService.get(employeeId);

        // Assert
        Assertions.assertEquals(expected, actual);

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    public void getNotExistingEmployeeById() {
        // Arrange
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows
                                                        (NotFoundException.class, () -> employeeService.get(employeeId));

        String expectedMessage = "Employee not found";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    public void createEmployee() throws IOException {
        // Arrange
        EmployeeArgument employeeArgument = readValueAction.execute
                                                                   ("jsons\\service\\employee\\create_employee_argument.json",
                                                                    CreateEmployeeArgument.class);

        Employee employeeForCreate = readValueAction.execute
                                                            ("jsons\\service\\employee\\employee_for_create.json",
                                                             Employee.class);

        Employee createdEmployee = readValueAction.execute
                                                          ("jsons\\service\\employee\\created_employee.json",
                                                           Employee.class);

        when(employeeMapper.toEmployee(employeeArgument)).thenReturn(employeeForCreate);
        when(employeeRepository.save(employeeForCreate)).thenReturn(createdEmployee);

        // Act
        Employee actual = employeeService.create(employeeArgument);

        // Assert
        Assertions.assertEquals(createdEmployee, actual);

        verify(employeeRepository).save(employeeForCreate);
    }

    @Test
    public void updateEmployee() throws IOException {
        // Arrange
        EmployeeArgument employeeArgument = readValueAction.execute
                                                                   ("jsons\\service\\employee\\update_employee_argument.json",
                                                                    UpdateEmployeeArgument.class);

        Employee employeeForUpdate = readValueAction.execute
                                                            ("jsons\\service\\employee\\employee_for_update.json",
                                                             Employee.class);

        Employee oldEmployee = readValueAction.execute
                                                      ("jsons\\service\\employee\\old_employee.json",
                                                       Employee.class);

        UUID updatedId = employeeForUpdate.getId();

        when(employeeMapper.toEmployee(employeeArgument, updatedId)).thenReturn(employeeForUpdate);
        when(employeeRepository.save(employeeForUpdate)).thenReturn(employeeForUpdate);
        when(employeeRepository.findById(updatedId)).thenReturn(Optional.of(oldEmployee));

        // Act
        Employee actual = employeeService.update(employeeArgument, updatedId);

        // Assert
        Assertions.assertEquals(employeeForUpdate, actual);

        verify(employeeRepository).save(employeeForUpdate);
        verify(employeeRepository).findById(updatedId);
    }

    @Test
    public void deleteEmployee() {
        // Arrange
        UUID deletedEmployeeId = UUID.randomUUID();

        doNothing().when(employeeRepository).deleteById(deletedEmployeeId);

        // Act
        employeeService.delete(deletedEmployeeId);

        // Assert
        verify(employeeRepository).deleteById(deletedEmployeeId);
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

