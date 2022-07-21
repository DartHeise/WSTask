package com.ws.task.service.employeeService;

import com.querydsl.jpa.impl.JPAQuery;
import com.ws.task.action.CreateJpaQueryAction;
import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.model.employee.QEmployee;
import com.ws.task.repository.EmployeeRepository;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    private final EmployeeRepository employeeRepository;

    private final CreateJpaQueryAction createJpaQueryAction;

    @Transactional(readOnly = true)
    public List<Employee> getAllOrdered(SearchingParameters searchParams) {
        JPAQuery<Employee> query = createJpaQueryAction.execute();
        QEmployee employee = QEmployee.employee;

        query.select(employee).from(employee);

        filterBySearchingParameters(searchParams, query, employee);

        return query.orderBy(employee.lastName.asc(),
                             employee.firstName.asc())
                    .fetch();
    }

    @Transactional(readOnly = true)
    public Employee get(UUID id) {
        return employeeRepository.findById(id)
                                 .orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    public Employee create(EmployeeArgument employeeArgument) {
        Employee createdEmployee = employeeMapper.toEmployee(employeeArgument);

        return employeeRepository.save(createdEmployee);
    }

    @Transactional
    public Employee update(EmployeeArgument employeeArgument, UUID id) {
        log.debug("Updating employee with id: {}", id);

        Employee updatedEmployee = employeeRepository.findById(id)
                                                     .orElseThrow(() -> new NotFoundException("Employee not found"));

        updateFields(updatedEmployee, employeeArgument);

        return employeeRepository.save(updatedEmployee);
    }

    @Transactional
    public void delete(UUID id) {
        employeeRepository.deleteById(id);
    }

    private void updateFields(Employee updatedEmployee, EmployeeArgument employeeArgument) {
        log.info("Updating fields...");
        if (!Objects.equals(updatedEmployee.getFirstName(), employeeArgument.getFirstName())) {
            log.debug("firstName: {} -> {}", updatedEmployee.getFirstName(), employeeArgument.getFirstName());
            updatedEmployee.setFirstName(employeeArgument.getFirstName());
        }
        if (!Objects.equals(updatedEmployee.getLastName(), employeeArgument.getLastName())) {
            log.debug("lastName: {} -> {}", updatedEmployee.getLastName(), employeeArgument.getLastName());
            updatedEmployee.setLastName(employeeArgument.getLastName());
        }
        if (!Objects.equals(updatedEmployee.getDescription(), employeeArgument.getDescription())) {
            log.debug("description: {} -> {}", updatedEmployee.getDescription(), employeeArgument.getDescription());
            updatedEmployee.setDescription(employeeArgument.getDescription());
        }
        if (!Objects.equals(updatedEmployee.getCharacteristics(), employeeArgument.getCharacteristics())) {
            log.debug("characteristics: {} -> {}", updatedEmployee.getCharacteristics(), employeeArgument.getCharacteristics());
            updatedEmployee.setCharacteristics(employeeArgument.getCharacteristics());
        }
        if (!Objects.equals(updatedEmployee.getContacts(), employeeArgument.getContacts())) {
            log.debug("contacts: {} -> {}", updatedEmployee.getContacts(), employeeArgument.getContacts());
            updatedEmployee.setContacts(employeeArgument.getContacts());
        }
        if (!Objects.equals(updatedEmployee.getJobType(), employeeArgument.getJobType())) {
            log.debug("jobType: {} -> {}", updatedEmployee.getJobType(), employeeArgument.getJobType());
            updatedEmployee.setJobType(employeeArgument.getJobType());
        }
        if (!Objects.equals(updatedEmployee.getPost(), employeeArgument.getPost())) {
            log.debug("post: {} -> {}", updatedEmployee.getPost(), employeeArgument.getPost());
            updatedEmployee.setPost(employeeArgument.getPost());
        }
    }

    private void filterBySearchingParameters(SearchingParameters searchParams, JPAQuery<Employee> query, QEmployee employee) {
        if (!StringUtils.isBlank(searchParams.getName())) {
            query.where(
                    employee.firstName.containsIgnoreCase(searchParams.getName()).or(
                            employee.lastName.containsIgnoreCase(searchParams.getName()))
                       );
        }
        if (searchParams.getPostId() != null) {
            query.where(
                    employee.post.id.eq(searchParams.getPostId())
                       );
        }
    }
}
