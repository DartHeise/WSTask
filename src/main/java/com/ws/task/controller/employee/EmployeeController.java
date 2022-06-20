package com.ws.task.controller.employee;

import com.ws.task.action.CreateEmployeeArgumentAction;
import com.ws.task.action.UpdateEmployeeArgumentAction;
import com.ws.task.controller.employee.dto.CreateEmployeeDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.dto.UpdateEmployeeDto;
import com.ws.task.controller.employee.mapper.EmployeeControllerMapper;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeControllerMapper employeeControllerMapper;

    private final CreateEmployeeArgumentAction createEmployeeArgAction;

    private final UpdateEmployeeArgumentAction updateEmployeeArgAction;

    @GetMapping("/{id}")
    @ApiOperation("Получить работника по идентификатору")
    public EmployeeDto getEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.get(id);
        return employeeControllerMapper.toEmployeeDto(employee, employee.getPost().getId());
    }

    @GetMapping("/getAll")
    @ApiOperation("Получить всех работников")
    public List<EmployeeDto> getAllEmployees(SearchingParameters searchingParams) {
        List<Employee> employees = employeeService.getAllOrdered(searchingParams);
        return employees.stream()
                .map(x -> employeeControllerMapper.toEmployeeDto(x, x.getPost().getId()))
                .toList();
    }

    @PostMapping("/create")
    @ApiOperation("Добавить работника")
    public EmployeeDto createEmployee(@RequestBody @Valid CreateEmployeeDto createEmployeeDto) {
        EmployeeArgument employeeArgument = createEmployeeArgAction.execute(createEmployeeDto);
        Employee createdEmployee = employeeService.create(employeeArgument);
        return employeeControllerMapper.toEmployeeDto(createdEmployee, createdEmployee.getPost().getId());
    }

    @PutMapping("/update/{id}")
    @ApiOperation("Обновить работника")
    public EmployeeDto updateEmployee(@PathVariable UUID id, @RequestBody @Valid UpdateEmployeeDto updateEmployeeDto) {
        EmployeeArgument employeeArgument = updateEmployeeArgAction.execute(updateEmployeeDto);
        Employee updatedEmployee = employeeService.update(employeeArgument, id);
        return employeeControllerMapper.toEmployeeDto(updatedEmployee, updatedEmployee.getPost().getId());
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удалить работника")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.delete(id);
    }
}
