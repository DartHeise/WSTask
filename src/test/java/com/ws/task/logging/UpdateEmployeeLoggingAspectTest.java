package com.ws.task.logging;

import ch.qos.logback.classic.Logger;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import com.ws.task.util.LogAppender;
import com.ws.task.util.ReadValueAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UpdateEmployeeLoggingAspectTest {

    private final EmployeeService employeeService = mock(EmployeeService.class);

    private final ReadValueAction readValueAction = new ReadValueAction();

    private LogAppender updateEmployeeFieldsLogAppender;

    private final UpdateEmployeeLoggingAspect updateEmployeeLoggingAspect = new UpdateEmployeeLoggingAspect(employeeService);

    @BeforeEach
    void setUp() {
        updateEmployeeFieldsLogAppender = new LogAppender();
        updateEmployeeFieldsLogAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(UpdateEmployeeLoggingAspect.class);
        logger.addAppender(updateEmployeeFieldsLogAppender);
    }

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

        assertApiRequestLog(updatedEmployeeId);
    }

    private void assertApiRequestLog(UUID id) {
        String updatedEmployeeIdLog = String.format("Updating employee with id: %s", id);

        assertUpdateEmployeeLog(updatedEmployeeIdLog, getUpdatedEmployeeFieldsLog());
    }

    private void assertUpdateEmployeeLog(String updatedEmployeeIdLog, String updatedEmployeeFieldsLog) {
        assertThat(updateEmployeeFieldsLogAppender.getLogEvents()).isNotEmpty()
                                                                  .anySatisfy(event -> assertThat(event.getMessage())
                                                                                .isEqualTo(updatedEmployeeFieldsLog))
                                                                  .anySatisfy(event -> assertThat(event.getMessage())
                                                                                    .isEqualTo(updatedEmployeeIdLog));

        updateEmployeeFieldsLogAppender.stop();
    }

    private String getUpdatedEmployeeFieldsLog() {
        return "Updating fields... " +
               "firstName: [Denis] -> [Artem] " +
               "lastName: [Losev] -> [Kornev] " +
               "description: [Lorem ipsum dolor sit amet] -> [] " +
               "characteristics: [[honest, introvert, like criticism, love of learning, pragmatism]] -> " +
               "[[shy, tactful, resourceful, reliable]] " +
               "contacts: [Contacts(phone=+16463484474, email=Losev14333@gmail.com, workEmail=LosevWorker111@bk.ru)] -> " +
               "[Contacts(phone=+16463483212, email=Kornevenrok@gmail.com, workEmail=KornevWorker123@bk.ru)] " +
               "post: [Post(id=854ef89d-6c27-4635-926d-894d76a81707, name=Backend)] -> " +
               "[Post(id=762d15a5-3bc9-43ef-ae96-02a680a557d0, name=Mobile)]";
    }
}
