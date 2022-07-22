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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        employeeRepository.findById(id)
                          .orElseThrow(() -> new NotFoundException("Employee not found"));

        Employee updatedEmployee = employeeMapper.toEmployee(employeeArgument, id);

        return employeeRepository.save(updatedEmployee);
    }

    @Transactional
    public void delete(UUID id) {
        employeeRepository.deleteById(id);
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
