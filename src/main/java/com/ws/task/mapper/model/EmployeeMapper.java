package com.ws.task.mapper.model;

import com.ws.task.model.employee.Employee;
import com.ws.task.service.employeeService.arguments.CreateEmployeeArgument;
import com.ws.task.service.employeeService.arguments.UpdateEmployeeArgument;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEmployee(CreateEmployeeArgument argument, UUID id);

    Employee toEmployee(UpdateEmployeeArgument argument, UUID id);
}
