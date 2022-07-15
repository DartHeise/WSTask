package com.ws.task.service.employeeService;

import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.exception.NotFoundException;
import com.ws.task.model.employee.Employee;
import com.ws.task.repository.EmployeeRepository;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllOrdered(SearchingParameters searchParams) {
        Stream<Employee> employeeStream = filterBySearchingParameters(searchParams);

        return employeeStream.sorted(Comparator.comparing(Employee::getLastName)
                                               .thenComparing(Employee::getFirstName))
                             .collect(Collectors.toList());
    }

    public Employee get(UUID id) {
        return employeeRepository.findById(id)
                                 .orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    public Employee create(EmployeeArgument employeeArgument) {
        Employee createdEmployee = employeeMapper.toEmployee(employeeArgument);

        return employeeRepository.save(createdEmployee);
    }

    public Employee update(EmployeeArgument employeeArgument, UUID id) {
        log.debug("Updating employee with id: {}", id);

        Employee updatedEmployee = employeeRepository.findById(id)
                                                     .orElseThrow(() -> new NotFoundException("Employee not found"));

        updateFields(updatedEmployee, employeeArgument);

        return employeeRepository.save(updatedEmployee);
    }

    public void delete(UUID id) {
        employeeRepository.deleteById(id);
    }

    private void updateFields(Employee updatedEmployee, EmployeeArgument employeeArgument) {
        log.info("Updating fields...");
        if (!Objects.equals(updatedEmployee.getFirstName(), employeeArgument.getFirstName())) {
            log.debug("first_name: {} -> {}", updatedEmployee.getFirstName(), employeeArgument.getFirstName());
            updatedEmployee.setFirstName(employeeArgument.getFirstName());
        }
        if (!Objects.equals(updatedEmployee.getLastName(), employeeArgument.getLastName())) {
            log.debug("last_name: {} -> {}", updatedEmployee.getLastName(), employeeArgument.getLastName());
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
            log.debug("job_type: {} -> {}", updatedEmployee.getJobType(), employeeArgument.getJobType());
            updatedEmployee.setJobType(employeeArgument.getJobType());
        }
        if (!Objects.equals(updatedEmployee.getPost(), employeeArgument.getPost())) {
            log.debug("post: {} -> {}", updatedEmployee.getPost(), employeeArgument.getPost());
            updatedEmployee.setPost(employeeArgument.getPost());
        }
    }

    private Stream<Employee> filterBySearchingParameters(SearchingParameters searchParams) {
        List<Employee> employees = new ArrayList<>(employeeRepository.findAll());
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
