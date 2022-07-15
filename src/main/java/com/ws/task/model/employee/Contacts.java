package com.ws.task.model.employee;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode
@Embeddable
public class Contacts {

    private String phone;

    private String email;

    private String workEmail;
}
