package com.mkappworks.controller.advice;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/throw-invalid-argument")
    public void throwInvalidArgumentException() {
        throw new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Invalid argument exception"));
    }

    @GetMapping("/throw-not-found")
    public void throwNotFoundException() {
        throw new StatusRuntimeException(Status.NOT_FOUND);
    }

    @GetMapping("/throw-internal-error")
    public void throwInternalErrorException() {
        throw new StatusRuntimeException(Status.INTERNAL.withDescription("An internal error occurred"));
    }
}
