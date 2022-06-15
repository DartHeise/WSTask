package com.ws.task.service.employeeService;

import com.ws.task.exception.NotFoundException;
import com.ws.task.mapper.model.EmployeeMapper;
import com.ws.task.mapper.model.EmployeeMapperImpl;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class EmployeeService {

    private final Map<UUID, Employee> employees = new HashMap<>();

    private final EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    public List<Employee> getAllOrdered(SearchingParameters searchParams) {

        List<Employee> employees = new ArrayList<>(this.employees.values());
        Stream<Employee> stream = employees.stream();

        if (!StringUtils.isBlank(searchParams.getName())) {
            stream = stream.filter(x -> StringUtils.containsIgnoreCase(x.getLastName(), searchParams.getName())
                                || StringUtils.containsIgnoreCase(x.getFirstName(), searchParams.getName()));
        }
        if (searchParams.getPostId() != null) {
            stream = stream.filter(x -> x.getPost().getId().equals(searchParams.getPostId()));
        }

        return stream.sorted(Comparator.comparing(Employee::getLastName)
                .thenComparing(Employee::getFirstName)).toList();
    }

    public Employee get(UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        return employees.get(id);
    }

    public Employee create(CreateEmployeeArgument createEmployeeArg) {
        Employee employee = employeeMapper.toEmployee(createEmployeeArg, UUID.randomUUID());
        employees.put(employee.getId(), employee);

        return employee;
    }

    public Employee update(UpdateEmployeeArgument updateEmployeeArg, UUID id) {
        throwNotFoundExceptionIfNotExists(id);

        Employee employee = employeeMapper.toEmployee(updateEmployeeArg, id);
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
            throw new NotFoundException();
    }
}
