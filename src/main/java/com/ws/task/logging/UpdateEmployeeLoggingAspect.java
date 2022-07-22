package com.ws.task.logging;

import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UpdateEmployeeLoggingAspect {

    private final EmployeeService employeeService;

    @Pointcut("execution(public * com.ws.task.service.employeeService.EmployeeService.update(..))")
    private void callUpdate() {

    }

    @Before("callUpdate() && " +
            "args(employeeArgument, id)")
    public void logUpdatedFields(JoinPoint joinPoint, EmployeeArgument employeeArgument, UUID id) {
        log.info("Updating employee with id: {}", id);

        Employee updatedEmployee = employeeService.get(id);

        updateFields(updatedEmployee, employeeArgument);
    }

    private void updateFields(Employee updatedEmployee, EmployeeArgument employeeArgument) {
        log.info("Updating fields...");

        if (!Objects.equals(updatedEmployee.getFirstName(), employeeArgument.getFirstName())) {
            log.info("firstName: {} -> {}", updatedEmployee.getFirstName(), employeeArgument.getFirstName());
        }
        if (!Objects.equals(updatedEmployee.getLastName(), employeeArgument.getLastName())) {
            log.info("lastName: {} -> {}", updatedEmployee.getLastName(), employeeArgument.getLastName());
        }
        if (!Objects.equals(updatedEmployee.getDescription(), employeeArgument.getDescription())) {
            log.info("description: {} -> {}", updatedEmployee.getDescription(), employeeArgument.getDescription());
        }
        if (!Objects.equals(updatedEmployee.getCharacteristics(), employeeArgument.getCharacteristics())) {
            log.info("characteristics: {} -> {}", updatedEmployee.getCharacteristics(), employeeArgument.getCharacteristics());
        }
        if (!Objects.equals(updatedEmployee.getContacts(), employeeArgument.getContacts())) {
            log.info("contacts: {} -> {}", updatedEmployee.getContacts(), employeeArgument.getContacts());
        }
        if (!Objects.equals(updatedEmployee.getJobType(), employeeArgument.getJobType())) {
            log.info("jobType: {} -> {}", updatedEmployee.getJobType(), employeeArgument.getJobType());
        }
        if (!Objects.equals(updatedEmployee.getPost(), employeeArgument.getPost())) {
            log.info("post: {} -> {}", updatedEmployee.getPost(), employeeArgument.getPost());
        }
    }
}
