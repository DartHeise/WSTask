package com.ws.task.model.employee.mapper;

import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.arguments.EmployeeArgument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface EmployeeMapper {

    Employee toEmployee(EmployeeArgument employeeArgument, UUID id);
}
