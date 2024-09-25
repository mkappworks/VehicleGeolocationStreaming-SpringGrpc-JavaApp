package com.mkappworks.service.advice;

import com.mkappworks.exceptions.StreamObserverException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ServiceExceptionHandler {

    @GrpcExceptionHandler(StreamObserverException.class)
    public Status handleStreamObserverException(StreamObserverException e) {
        return Status.ABORTED.withDescription(e.getMessage()).withCause(e);
    }

}
