package org.oem.pinggo.eventBased;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HelloListenerAnot {
    @EventListener
    public void handleEvent(HelloEvent event) {
        System.out.println("The Anotbased event occurred with the message " + event.getMessage());
    }
}