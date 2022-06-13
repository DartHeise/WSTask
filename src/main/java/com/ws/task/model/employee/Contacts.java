package com.ws.task.model.employee;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@EqualsAndHashCode
public class Contacts {

    private String phone;

    private String email;

    private String workEmail;
}
