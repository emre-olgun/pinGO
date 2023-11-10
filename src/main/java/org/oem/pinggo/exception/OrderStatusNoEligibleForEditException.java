package org.oem.pinggo.exception;

public class OrderStatusNoEligibleForEditException extends RuntimeException {

    public OrderStatusNoEligibleForEditException(String message){
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}