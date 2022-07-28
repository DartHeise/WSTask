package com.ws.task.repository;

import com.ws.task.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID>,
                                            QuerydslPredicateExecutor<Employee> {
}
