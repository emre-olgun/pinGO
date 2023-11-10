package org.oem.pinggo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchElementFoundException extends RuntimeException {

    public NoSuchElementFoundException(String message){
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}