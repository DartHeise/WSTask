package com.ws.task.controller.employee;

import com.ws.task.action.CreateEmployeeArgumentAction;
import com.ws.task.action.UpdateEmployeeArgumentAction;
import com.ws.task.controller.employee.dto.CreateEmployeeArgumentDto;
import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.controller.employee.dto.UpdateEmployeeArgumentDto;
import com.ws.task.controller.employee.mapper.EmployeeDtoMapper;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.EmployeeService;
import com.ws.task.service.employeeService.SearchingParameters;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
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

    private final UpdateEmployeeArgumentAction updateEmployeeArgAction;

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
    public EmployeeDto updateEmployee(@PathVariable UUID id, @RequestBody @Valid UpdateEmployeeArgumentDto updateEmployeeArgDto) {
        UpdateEmployeeArgument updateEmployeeArg = updateEmployeeArgAction.execute(updateEmployeeArgDto);
        Employee updatedEmployee = employeeService.update(updateEmployeeArg, id);
        return employeeDtoMapper.toEmployeeDto(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Удалить работника")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.delete(id);
    }
}
