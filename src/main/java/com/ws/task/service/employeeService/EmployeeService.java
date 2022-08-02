package com.ws.task.service.employeeService;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.model.employee.QEmployee;
import com.ws.task.repository.EmployeeRepository;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import com.ws.task.util.Guard;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    private final EmployeeRepository employeeRepository;

    private final QEmployee qEmployee = QEmployee.employee;

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Employee> getAllOrdered(SearchingParameters searchParams, Sort sort) {
        Predicate nameAndPostIdPredicate = filterBySearchingParameters(searchParams);

        return Lists.newArrayList(employeeRepository.findAll(nameAndPostIdPredicate, sort));
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Employee get(UUID id) {
        return employeeRepository.findById(id)
                                 .orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    public Employee create(EmployeeArgument employeeArgument) {
        Employee createdEmployee = employeeMapper.toEmployee(employeeArgument);

        return employeeRepository.save(createdEmployee);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Employee update(EmployeeArgument employeeArgument, UUID id) {
        Guard.check(employeeRepository.existsById(id), "Employee not found");

        Employee updatedEmployee = employeeMapper.toEmployee(employeeArgument, id);

        return employeeRepository.save(updatedEmployee);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(UUID id) {
        employeeRepository.deleteById(id);
    }

    private Predicate filterBySearchingParameters(SearchingParameters searchParams) {
        BooleanBuilder searchParamsBooleanBuilder = new BooleanBuilder();

        if (!StringUtils.isBlank(searchParams.getName())) {
            searchParamsBooleanBuilder.and(qEmployee.firstName.containsIgnoreCase(searchParams.getName()).or(
                    qEmployee.lastName.containsIgnoreCase(searchParams.getName())));
        }
        if (searchParams.getPostId() != null) {
            searchParamsBooleanBuilder.and(qEmployee.post.id.eq(searchParams.getPostId()));
        }

        return searchParamsBooleanBuilder;
    }
}
