package com.ws.task.util;

import com.ws.task.exception.NotFoundException;

public class Guard {

    public static void check(boolean condition, String message) {
        if (!condition)
            throw new NotFoundException(message);
    }
}
