package com.ws.task.logging;

import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.util.ReadValueAction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateEmployeeLoggingAspectTest {

    private final ReadValueAction readValueAction = new ReadValueAction();
    @InjectMocks
    private final UpdateEmployeeLoggingAspect updateEmployeeLoggingAspect = new UpdateEmployeeLoggingAspect(employeeService);

    private final EmployeeService employeeService = mock(EmployeeService.class);
    @Mock
    private Logger logger;

    @Test
    void loggingUpdatedEmployeeFields() throws IOException {
        // Arrange
        Employee updatedEmployee = readValueAction.execute
                                                          ("jsons\\logging\\updateEmployee\\updated_employee.json",
                                                           Employee.class);

        EmployeeArgument employeeArgument = readValueAction.execute
                                                                   ("jsons\\logging\\updateEmployee\\update_employee_argument.json",
                                                                    UpdateEmployeeArgument.class);

        UUID updatedEmployeeId = updatedEmployee.getId();

        when(employeeService.get(updatedEmployeeId)).thenReturn(updatedEmployee);

        // Act
        updateEmployeeLoggingAspect.loggingUpdatedFields(employeeArgument, updatedEmployeeId);

        // Assert
        verify(employeeService).get(updatedEmployeeId);

        verify(logger).info("Updating employee with id: {}", updatedEmployeeId);
        verify(logger).info("Updating fields... " +
                            "firstName: [Denis] -> [Artem] " +
                            "lastName: [Losev] -> [Kornev] " +
                            "description: [Lorem ipsum dolor sit amet] -> [] " +
                            "characteristics: [[honest, introvert, like criticism, love of learning, pragmatism]] -> " +
                            "[[shy, tactful, resourceful, reliable]] " +
                            "contacts: [Contacts(phone=+16463484474, email=Losev14333@gmail.com, workEmail=LosevWorker111@bk.ru)] -> " +
                            "[Contacts(phone=+16463483212, email=Kornevenrok@gmail.com, workEmail=KornevWorker123@bk.ru)] " +
                            "post: [Post(id=854ef89d-6c27-4635-926d-894d76a81707, name=Backend)] -> " +
                            "[Post(id=762d15a5-3bc9-43ef-ae96-02a680a557d0, name=Mobile)]");
        verifyNoMoreInteractions(logger);
    }
}
