package com.ws.task.controller.employee.mapper;

import com.ws.task.controller.employee.dto.EmployeeDto;
import com.ws.task.model.employee.Employee;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface EmployeeControllerMapper {

    EmployeeDto toEmployeeDto(Employee employee, UUID postId);
}
