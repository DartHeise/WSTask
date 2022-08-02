package com.ws.task.service.employeeService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchingParameters {

    private String name;

    private UUID postId;
}
