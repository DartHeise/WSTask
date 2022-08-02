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
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(SoftAssertionsExtension.class)
public class EmployeeServiceTest {

    private final Sort sort = mock(Sort.class);

    private final EmployeeMapper employeeMapper = mock(EmployeeMapper.class);

    private final EmployeeRepository employeeRepository = mock(EmployeeRepository.class);

    private final ReadValueAction readValueAction = new ReadValueAction();

    private final EmployeeService employeeService = new EmployeeService(employeeMapper, employeeRepository);

    private static Stream<Arguments> getAllOrdered() {
        return Stream.of(
                Arguments.of("jsons\\service\\employee\\sorted_employees.json",
                             null, null,
                             "com.querydsl.core.BooleanBuilder@0"),

                Arguments.of("jsons\\service\\employee\\employees_with_backend_id.json",
                             null, UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"),
                             "employee.post.id = 854ef89d-6c27-4635-926d-894d76a81707"),

                Arguments.of("jsons\\service\\employee\\employees_with_first_name_ivan.json",
                             "Ivan", null,
                             "containsIc(employee.firstName,Ivan) || containsIc(employee.lastName,Ivan)"),

                Arguments.of("jsons\\service\\employee\\employees_with_last_name_ivanov.json",
                             "Ivanov", null,
                             "containsIc(employee.firstName,Ivanov) || " +
                             "containsIc(employee.lastName,Ivanov)"),

                Arguments.of("jsons\\service\\employee\\employees_with_first_name_denis_and_backend_id.json",
                             "Denis", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"),
                             "(containsIc(employee.firstName,Denis) || " +
                             "containsIc(employee.lastName,Denis)) && " +
                             "employee.post.id = 854ef89d-6c27-4635-926d-894d76a81707"),

                Arguments.of("jsons\\service\\employee\\employees_with_last_name_losev_and_backend_id.json",
                             "Losev", UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"),
                             "(containsIc(employee.firstName,Losev) || " +
                             "containsIc(employee.lastName,Losev)) && " +
                             "employee.post.id = 854ef89d-6c27-4635-926d-894d76a81707")
                        );
    }

    @MethodSource
    @ParameterizedTest
    public void getAllOrdered(String path, String name, UUID postId, String expectedPredicate) throws Exception {
        // Arrange
        List<Employee> expected = readValueAction.execute
                                                         (path, new TypeReference<>() {});

        when(employeeRepository.findAll(any(Predicate.class), eq(sort))).thenReturn(expected);

        // Act
        List<Employee> actual = employeeService.getAllOrdered
                                                       (new SearchingParameters(name, postId), sort);

        // Assert
        Assertions.assertEquals(expected, actual);

        ArgumentCaptor<Predicate> predicateArgumentCaptor = ArgumentCaptor.forClass(Predicate.class);
        verify(employeeRepository).findAll(predicateArgumentCaptor.capture(), eq(sort));

        String actualPredicate = predicateArgumentCaptor.getValue().toString();
        Assertions.assertEquals(actualPredicate, expectedPredicate);
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
        verify(employeeMapper).toEmployee(employeeArgument);
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

        UUID updatedId = employeeForUpdate.getId();

        when(employeeMapper.toEmployee(employeeArgument, updatedId)).thenReturn(employeeForUpdate);
        when(employeeRepository.save(employeeForUpdate)).thenReturn(employeeForUpdate);
        when(employeeRepository.existsById(updatedId)).thenReturn(true);

        // Act
        Employee actual = employeeService.update(employeeArgument, updatedId);

        // Assert
        Assertions.assertEquals(employeeForUpdate, actual);

        verify(employeeRepository).existsById(updatedId);
        verify(employeeRepository).save(employeeForUpdate);
        verify(employeeMapper).toEmployee(employeeArgument, updatedId);
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
}

