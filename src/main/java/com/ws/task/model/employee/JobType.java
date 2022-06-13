package com.ws.task.model.employee;

import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
public enum JobType {

    PERMANENT,
    TEMPORARY,
    CONTRACT,
    FULL_TIME,
    PART_TIME
}
