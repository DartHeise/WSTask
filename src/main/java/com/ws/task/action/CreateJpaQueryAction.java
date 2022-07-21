package com.ws.task.action;

import com.querydsl.jpa.impl.JPAQuery;
import com.ws.task.model.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class CreateJpaQueryAction {

    @PersistenceContext
    private final EntityManager entityManager;

    public JPAQuery<Employee> execute() {
        return new JPAQuery<>(entityManager);
    }
}
