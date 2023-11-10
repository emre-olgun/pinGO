package org.oem.pinggo.eventBased;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class HelloListenerOne implements ApplicationListener<HelloEvent> {

    @Override
    public void onApplicationEvent(HelloEvent event) {
        System.out.println(event.getSource());
        System.out.println("First listener handled the event, the message is " + event.getMessage()+event.getSuffix());
    }
}

