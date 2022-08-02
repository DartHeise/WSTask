package com.ws.task.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ReadValueAction {

    ObjectMapper objectMapper = new ObjectMapper();

    public <T> T execute(String name, Class<T> valueType) throws IOException {
        return objectMapper.readValue
                                   (getClass().getClassLoader().getResource(name), valueType);
    }

    public <T> T execute(String name, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue
                                   (getClass().getClassLoader().getResource(name), valueTypeRef);
    }
}
