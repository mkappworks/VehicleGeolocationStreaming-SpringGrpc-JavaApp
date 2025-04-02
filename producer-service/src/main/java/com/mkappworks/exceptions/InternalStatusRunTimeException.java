package com.mkappworks.exceptions;

public class InternalStatusRunTimeException extends RuntimeException {

    private static final String MESSAGE = "Internal server error";

    public InternalStatusRunTimeException() {
        super(MESSAGE);
    }

}
