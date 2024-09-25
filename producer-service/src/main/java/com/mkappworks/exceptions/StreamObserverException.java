package com.mkappworks.exceptions;

import lombok.Getter;

@Getter
public class StreamObserverException extends RuntimeException {

    private static final String MESSAGE = "Error with stream observer";
    private final Throwable cause;

    public StreamObserverException(Throwable cause) {
        super(MESSAGE);
        this.cause = cause;
    }

}
