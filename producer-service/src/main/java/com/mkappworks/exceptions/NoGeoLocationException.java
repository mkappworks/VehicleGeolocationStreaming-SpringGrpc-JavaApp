package com.mkappworks.exceptions;

public class NoGeoLocationException extends RuntimeException {

    private static final String MESSAGE = "GeoLocation not found for vehicle";

    public NoGeoLocationException() {
        super(MESSAGE);
    }
}
