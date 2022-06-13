package com.ws.task.mapper.dto;

import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.model.employee.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeDtoMapper {

    EmployeeDto toEmployeeDto(Employee createEmployeeDto);
}
