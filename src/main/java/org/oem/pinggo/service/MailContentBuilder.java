package org.oem.pinggo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class MailContentBuilder {


    public String build(String message) {
return message;
    }
}