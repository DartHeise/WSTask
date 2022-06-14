package com.ws.task.controller.employee;

import com.ws.task.action.CreateEmployeeArgumentAction;
import com.ws.task.controller.employee.dto.CreateEmployeeArgumentDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.mapper.dto.EmployeeDtoMapper;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.CreateEmployeeArgument;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
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

    private final EmployeeDtoMapper employeeDtoMapper;

    private final CreateEmployeeArgumentAction createEmployeeArgAction;

    @GetMapping("/get/{id}")
    @ApiOperation("Получить работника по идентификатору")
    public EmployeeDto getEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.get(id);
        return employeeDtoMapper.toEmployeeDto(employee);
    }

    @PostMapping("/getAll")
    @ApiOperation("Получить всех работников")
    public List<EmployeeDto> getAllEmployees(@RequestBody SearchingParameters searchingParams) {
        List<Employee> employees = employeeService.getAllOrdered
                (searchingParams);
        return employees.stream()
                .map(employeeDtoMapper :: toEmployeeDto)
                .toList();
    }

    @PostMapping("/create")
    @ApiOperation("Добавить работника")
    public EmployeeDto createEmployee(@RequestBody @Valid CreateEmployeeArgumentDto createEmployeeArgDto) {
        CreateEmployeeArgument createEmployeeArg = createEmployeeArgAction.execute(createEmployeeArgDto);
        Employee createdEmployee = employeeService.create(createEmployeeArg);
        return employeeDtoMapper.toEmployeeDto(createdEmployee);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("Обновить работника")
    public EmployeeDto updateEmployee(@PathVariable UUID id, @RequestBody @Valid CreateEmployeeArgument createEmployeeArg) {
        Employee updatedEmployee = employeeService.update(createEmployeeArg, id);
        return employeeDtoMapper.toEmployeeDto(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удалить работника")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.delete(id);
    }
}
