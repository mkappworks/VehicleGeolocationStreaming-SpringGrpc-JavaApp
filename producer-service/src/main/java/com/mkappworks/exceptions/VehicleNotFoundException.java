package com.mkappworks.exceptions;

public class VehicleNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Vehicle not found";

    public VehicleNotFoundException() {
        super(MESSAGE);
    }
}
