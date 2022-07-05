package com.ws.task.controller.employee.mapper;

import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface EmployeeMapper {

    EmployeeDto toEmployeeDto(Employee employee, UUID postId);

    Employee toEmployee(EmployeeArgument employeeArgument, UUID id);
}
