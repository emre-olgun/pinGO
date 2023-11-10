package org.oem.pinggo.eventBased;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class HelloListenerTwo implements ApplicationListener<HelloEvent> {

    @Override
    public void onApplicationEvent(HelloEvent event) {
        System.out.println("Second listener handled the event, the message is " + event.getMessage());
        ;
    }
}
