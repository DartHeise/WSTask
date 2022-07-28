package com.ws.task.logging;

import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class UpdateEmployeeLoggingAspectTest {

    private final Employee updatedEmployee = mock(Employee.class);

    private final EmployeeService employeeService = mock(EmployeeService.class);

    private final EmployeeArgument employeeArgument = mock(EmployeeArgument.class);

    private final UUID id = UUID.fromString("8ee05ef8-eed9-11ec-8ea0-0242ac120002");

    private final UpdateEmployeeLoggingAspect updateEmployeeLoggingAspect = new UpdateEmployeeLoggingAspect(employeeService);

    @Test
    void loggingUpdatedEmployeeFields() {
        // Arrange
        when(employeeService.get(id)).thenReturn(updatedEmployee);

        // Act
        updateEmployeeLoggingAspect.loggingUpdatedFields(employeeArgument, id);

        // Assert
        verify(employeeService).get(id);
    }
}
