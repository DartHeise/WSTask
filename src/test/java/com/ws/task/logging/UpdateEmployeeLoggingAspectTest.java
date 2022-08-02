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
    private final UpdateEmployeeLoggingAspect updateEmployeeLoggingAspect = new UpdateEmployeeLoggingAspect(employeeService);
    private LogAppender updateEmployeeFieldsLogAppender;
    private Employee updatedEmployee;
    private EmployeeArgument employeeArgument;

    @BeforeEach
    void setUp() throws IOException {
        updateEmployeeFieldsLogAppender = new LogAppender();
        updateEmployeeFieldsLogAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(UpdateEmployeeLoggingAspect.class);
        logger.addAppender(updateEmployeeFieldsLogAppender);

        updatedEmployee = readValueAction.execute
                                                 ("jsons\\logging\\updateEmployee\\updated_employee.json",
                                                  Employee.class);

        employeeArgument = readValueAction.execute
                                                  ("jsons\\logging\\updateEmployee\\update_employee_argument.json",
                                                   UpdateEmployeeArgument.class);
    }

    @Test
    void loggingUpdatedEmployeeFields() {
        // Arrange
        UUID updatedEmployeeId = updatedEmployee.getId();

        when(employeeService.get(updatedEmployeeId)).thenReturn(updatedEmployee);

        // Act
        updateEmployeeLoggingAspect.loggingUpdatedFields(employeeArgument, updatedEmployeeId);

        // Assert
        verify(employeeService).get(updatedEmployeeId);

        assertUpdateEmployeeLog(updatedEmployeeId);
    }

    private void assertUpdateEmployeeLog(UUID id) {
        String updatedEmployeeIdLog = String.format("Updating employee with id: %s", id);
        String updatedEmployeeFieldsLog = getUpdatedEmployeeFieldsLog();

        assertThat(updateEmployeeFieldsLogAppender.getLogEvents()).isNotEmpty()
                                                                  .anySatisfy(event -> assertThat(event.getMessage())
                                                                          .isEqualTo(updatedEmployeeFieldsLog))
                                                                  .anySatisfy(event -> assertThat(event.getMessage())
                                                                          .isEqualTo(updatedEmployeeIdLog));

        updateEmployeeFieldsLogAppender.stop();
    }

    private String getUpdatedEmployeeFieldsLog() {
        StringBuilder sb = new StringBuilder();

        sb.append("Updating fields... ")
          .append(String.format("firstName: [%s] -> [%s] ",
                                updatedEmployee.getFirstName(), employeeArgument.getFirstName()))
          .append(String.format("lastName: [%s] -> [%s] ",
                                updatedEmployee.getLastName(), employeeArgument.getLastName()))
          .append(String.format("description: [%s] -> [%s] ",
                                updatedEmployee.getDescription(), employeeArgument.getDescription()))
          .append(String.format("characteristics: [%s] -> [%s] ",
                                updatedEmployee.getCharacteristics(), employeeArgument.getCharacteristics()))
          .append(String.format("contacts: [%s] -> [%s] ",
                                updatedEmployee.getContacts(), employeeArgument.getContacts()))
          .append(String.format("jobType: [%s] -> [%s] ",
                                updatedEmployee.getJobType(), employeeArgument.getJobType()))
          .append(String.format("post: [%s] -> [%s]",
                                updatedEmployee.getPost(), employeeArgument.getPost()));

        return sb.toString();
    }
}
