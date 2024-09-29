package com.mkappworks.service.advice;

import com.mkappworks.exceptions.InternalStatusRunTimeException;
import com.mkappworks.exceptions.NoGeoLocationException;
import com.mkappworks.exceptions.UnKnownStatusRunTimeException;
import com.mkappworks.exceptions.VehicleNotFoundException;

import io.grpc.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ServiceExceptionHandlerTest {

    @InjectMocks
    private ServiceExceptionHandler serviceExceptionHandler;

    @Test
    void handle_internal_status_runtime_exception_should_return_internal_status() {
        InternalStatusRunTimeException exception = new InternalStatusRunTimeException();

        Status result = serviceExceptionHandler.handleInternalStatusRunTimeException(exception);

        assertEquals(Status.INTERNAL.getCode(), result.getCode(), "Expected INTERNAL gRPC status");
        assertEquals("Internal server error", result.getDescription(), "Expected correct exception message in description");
    }

    @Test
    void handle_unknown_status_runtime_exception_should_return_unknown_status() {
        UnKnownStatusRunTimeException exception = new UnKnownStatusRunTimeException();

        Status result = serviceExceptionHandler.handleStreamObserverException(exception);

        assertEquals(Status.UNKNOWN.getCode(), result.getCode(), "Expected UNKNOWN gRPC status");
        assertEquals("An unknown error occurred", result.getDescription(), "Expected correct exception message in description");
    }

    @Test
    void handle_no_geo_location_exception_should_return_not_found_status() {
        NoGeoLocationException exception = new NoGeoLocationException();

        Status result = serviceExceptionHandler.handleNoGeoLocationException(exception);

        assertEquals(Status.NOT_FOUND.getCode(), result.getCode(), "Expected NOT_FOUND gRPC status");
        assertEquals("GeoLocation not found for vehicle", result.getDescription(), "Expected correct exception message in description");
    }

    @Test
    void handle_vehicle_not_found_exception_should_return_not_found_status() {
        VehicleNotFoundException exception = new VehicleNotFoundException();

        Status result = serviceExceptionHandler.handleVehicleNotFoundException(exception);

        assertEquals(Status.NOT_FOUND.getCode(), result.getCode(), "Expected NOT_FOUND gRPC status");
        assertEquals("Vehicle not found", result.getDescription(), "Expected correct exception message in description");
    }
}
