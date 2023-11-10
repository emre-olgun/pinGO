package org.oem.pinggo.exception;

public class ItemOwnerException extends RuntimeException {

    public ItemOwnerException(String message){
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}