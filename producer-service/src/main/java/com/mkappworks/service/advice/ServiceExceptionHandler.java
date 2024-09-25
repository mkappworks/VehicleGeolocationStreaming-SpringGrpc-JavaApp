package com.mkappworks.service.advice;

import com.mkappworks.exceptions.NoGeoLocationException;
import com.mkappworks.exceptions.InternalStatusRunTimeException;
import com.mkappworks.exceptions.UnKnownStatusRunTimeException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ServiceExceptionHandler {

    @GrpcExceptionHandler(InternalStatusRunTimeException.class)
    public Status handleInternalStatusRunTimeException(InternalStatusRunTimeException e) {
        return Status.INTERNAL.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(UnKnownStatusRunTimeException.class)
    public Status handleStreamObserverException(UnKnownStatusRunTimeException e) {
        return Status.UNKNOWN.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(NoGeoLocationException.class)
    public Status handleNoGeoLocationException(NoGeoLocationException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }
}
