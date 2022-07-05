package com.ws.task.service.employeeService;

import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final Map<UUID, Employee> employees;

    private final EmployeeMapper employeeMapper;

    public void addEmployees(List<Employee> employeeList) {
        employeeList.stream().forEach(x -> employees.put(x.getId(), x));
    }

    public List<Employee> getAllOrdered(SearchingParameters searchParams) {
        Stream<Employee> employeeStream = filterBySearchingParameters(searchParams);

        return employeeStream.sorted(Comparator.comparing(Employee::getLastName)
                .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
    }

    public Employee get(UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        return employees.get(id);
    }

    public Employee create(EmployeeArgument employeeArgument) {
        Employee employee = employeeMapper.toEmployee(employeeArgument, UUID.randomUUID());
        employees.put(employee.getId(), employee);

        return employee;
    }

    public Employee update(EmployeeArgument employeeArgument, UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        Employee employee = employeeMapper.toEmployee(employeeArgument, id);
        employees.replace(employee.getId(), employee);

        return employee;
    }

    public void delete(UUID id) {
        employees.remove(id);
    }

    public void deleteAll() {
        employees.clear();
    }

    private void throwNotFoundExceptionIfNotExists(UUID id) {
        if (!employees.containsKey(id))
            throw new NotFoundException("Employee not found");
    }

    private Stream<Employee> filterBySearchingParameters(SearchingParameters searchParams) {
        List<Employee> employees = new ArrayList<>(this.employees.values());
        Stream<Employee> employeeStream = employees.stream();

        if (!StringUtils.isBlank(searchParams.getName())) {
            employeeStream = employeeStream.filter(x -> StringUtils.containsIgnoreCase(x.getLastName(), searchParams.getName())
                    || StringUtils.containsIgnoreCase(x.getFirstName(), searchParams.getName()));
        }
        if (searchParams.getPostId() != null) {
            employeeStream = employeeStream.filter(x -> x.getPost().getId().equals(searchParams.getPostId()));
        }

        return employeeStream;
    }
}
