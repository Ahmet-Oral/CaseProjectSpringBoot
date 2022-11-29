package com.ahmetoral.inventorymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class}) // selected exception will be fed
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        // create payload containing the exception details
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z")) // UTC - Turkey is 3 hours ahead of UTC
        );
        // return response entity
        return new ResponseEntity<>(apiException, badRequest);
    }
}
