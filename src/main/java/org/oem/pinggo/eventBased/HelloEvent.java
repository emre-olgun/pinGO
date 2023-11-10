package org.oem.pinggo.eventBased;

import org.springframework.context.ApplicationEvent;

public class HelloEvent extends ApplicationEvent {

    private final String message;
    private final String suffix ;

    public HelloEvent(Object source, String message,String suffix) {
        super(source);
        this.message = message;
        this.suffix=suffix;
    }

    public String getMessage() {
        return message;
    }

    public String getSuffix() {
        return suffix;
    }
}