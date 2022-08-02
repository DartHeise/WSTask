package com.ws.task.logging;

import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "logging", name = "employee.update")
public class UpdateEmployeeLoggingAspect {

    private final EmployeeService employeeService;

    @Pointcut("execution(public * com.ws.task.service.employeeService.EmployeeService.update(..))")
    private void callUpdate() {

    }

    @Before("callUpdate() && " +
            "args(employeeArgument, id)")
    public void loggingUpdatedFields(EmployeeArgument employeeArgument, UUID id) {
        log.info("Updating employee with id: " + id);

        Employee updatedEmployee = employeeService.get(id);

        updateFields(updatedEmployee, employeeArgument);
    }

    private void updateFields(Employee updatedEmployee, EmployeeArgument employeeArgument) {
        StringBuilder sb = new StringBuilder("Updating fields...");

        sb.append(appendUpdatedField("firstName",
                                     updatedEmployee.getFirstName(), employeeArgument.getFirstName()))
          .append(appendUpdatedField("lastName",
                                     updatedEmployee.getLastName(), employeeArgument.getLastName()))
          .append(appendUpdatedField("description",
                                     updatedEmployee.getDescription(), employeeArgument.getDescription()))
          .append(appendUpdatedField("characteristics",
                                     updatedEmployee.getCharacteristics(), employeeArgument.getCharacteristics()))
          .append(appendUpdatedField("contacts",
                                     updatedEmployee.getContacts(), employeeArgument.getContacts()))
          .append(appendUpdatedField("jobType",
                                     updatedEmployee.getJobType(), employeeArgument.getJobType()))
          .append(appendUpdatedField("post",
                                     updatedEmployee.getPost(), employeeArgument.getPost()));

        log.info(sb.toString());
    }

    private String appendUpdatedField(String argumentName, Object oldArgument, Object newArgument) {
        return Objects.equals(oldArgument, newArgument)
               ? ""
               : String.format(" %s: [%s] -> [%s]", argumentName, oldArgument, newArgument);
    }
}
