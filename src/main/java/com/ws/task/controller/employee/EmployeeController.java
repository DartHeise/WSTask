package com.ws.task.controller.employee;

import com.ws.task.action.CreateEmployeeArgumentAction;
import com.ws.task.action.UpdateEmployeeArgumentAction;
import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.dto.UpdateEmployeeDto;
import com.ws.task.controller.employee.mapper.EmployeeMapper;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    private final CreateEmployeeArgumentAction createEmployeeArgumentCreator;

    private final UpdateEmployeeArgumentAction updateEmployeeArgumentCreator;

    @GetMapping("/{id}")
    @ApiOperation("Получить работника по идентификатору")
    public EmployeeDto getEmployee(@PathVariable UUID id, HttpServletRequest request) {
        log.debug("Request getEmployee with params:");
        log.debug("id: {}", id);
        log.debug("request IP: {}", request.getRemoteAddr());
        Employee employee = employeeService.get(id);
        return employeeMapper.toEmployeeDto(employee, employee.getPost().getId());
    }

    @GetMapping("/getAll")
    @ApiOperation("Получить всех работников")
    public List<EmployeeDto> getAllEmployees(SearchingParameters searchingParams, HttpServletRequest request) {
        log.debug("Request getAllEmployees with params:");
        log.debug("searchingParams: {}", searchingParams);
        log.debug("request IP: {}", request.getRemoteAddr());
        List<Employee> employees = employeeService.getAllOrdered(searchingParams);
        return employees.stream()
                        .map(x -> employeeMapper.toEmployeeDto(x, x.getPost().getId()))
                        .collect(Collectors.toList());
    }

    @PostMapping("/create")
    @ApiOperation("Добавить работника")
    public EmployeeDto createEmployee(@RequestBody @Valid CreateEmployeeDto createEmployeeDto, HttpServletRequest request) {
        log.debug("Request createEmployee with params:");
        log.debug("createEmployeeDto: {}", createEmployeeDto);
        log.debug("request IP: {}", request.getRemoteAddr());
        EmployeeArgument employeeArgument = createEmployeeArgumentCreator.execute(createEmployeeDto);
        Employee createdEmployee = employeeService.create(employeeArgument);
        return employeeMapper.toEmployeeDto(createdEmployee, createdEmployee.getPost().getId());
    }

    @PutMapping("/{id}/update")
    @ApiOperation("Обновить работника")
    public EmployeeDto updateEmployee(@PathVariable UUID id, @RequestBody @Valid UpdateEmployeeDto updateEmployeeDto, HttpServletRequest request) {
        log.debug("Request updateEmployee with params:");
        log.debug("id: {}", id);
        log.debug("updateEmployeeDto: {}", updateEmployeeDto);
        log.debug("request IP: {}", request.getRemoteAddr());
        EmployeeArgument employeeArgument = updateEmployeeArgumentCreator.execute(updateEmployeeDto);
        Employee updatedEmployee = employeeService.update(employeeArgument, id);
        return employeeMapper.toEmployeeDto(updatedEmployee, updatedEmployee.getPost().getId());
    }

    @DeleteMapping("/{id}/delete")
    @ApiOperation("Удалить работника")
    public void deleteEmployee(@PathVariable UUID id, HttpServletRequest request) {
        log.debug("Request deleteEmployee with params:");
        log.debug("id: {}", id);
        log.debug("request IP: {}", request.getRemoteAddr());
        employeeService.delete(id);
    }
}
