package com.ahmetoral.inventorymanagement.exception;

public class ApiRequestException extends RuntimeException{

    public ApiRequestException(String message) {
        super(message);
    }

}
