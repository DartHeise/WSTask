package com.ws.task.model.employee;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Getter
@ToString
@Embeddable
@EqualsAndHashCode
public class Contacts {

    private String phone;

    private String email;

    private String workEmail;
}
