package com.mkappworks.service.advice;

import com.mkappworks.exceptions.InternalStatusRunTimeException;
import com.mkappworks.exceptions.UnKnownStatusRunTimeException;
import com.mkappworks.exceptions.VehicleNotFoundException;

import io.grpc.Status;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(InternalStatusRunTimeException.class)
    public Status handleInternalStatusRunTimeException(InternalStatusRunTimeException e) {
        return Status.INTERNAL.withDescription(e.getMessage());
    }

    @ExceptionHandler(UnKnownStatusRunTimeException.class)
    public Status handleStreamObserverException(UnKnownStatusRunTimeException e) {
        return Status.UNKNOWN.withDescription(e.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public Status handleVehicleNotFoundException(VehicleNotFoundException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }
}
