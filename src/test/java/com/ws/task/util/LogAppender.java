package com.ws.task.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LogAppender extends AppenderBase<ILoggingEvent> {

    private final List<ILoggingEvent> logEvents = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        logEvents.add(iLoggingEvent);
    }
}
