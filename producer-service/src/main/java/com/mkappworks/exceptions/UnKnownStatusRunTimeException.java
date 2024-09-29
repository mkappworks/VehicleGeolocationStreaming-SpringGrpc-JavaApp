package com.mkappworks.exceptions;

public class UnKnownStatusRunTimeException extends RuntimeException {

    private static final String MESSAGE = "An unknown error occurred";

    public UnKnownStatusRunTimeException() {
        super(MESSAGE);
    }
}
