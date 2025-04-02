package com.mkappworks.service;

import com.mkappworks.config.GeoLocationProperties;
import com.mkappworks.exceptions.InternalStatusRunTimeException;
import com.mkappworks.exceptions.VehicleNotFoundException;
import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.service.handlers.GeoLocationHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.ScheduledExecutorService;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceGrpcImplTest {

    @InjectMocks
    private GeoLocationServiceGrpcImpl geoLocationServiceGrpc;

    @Mock
    private GeoLocationHandler geoLocationHandler;

    @Mock
    private ScheduledExecutorService scheduler;

    @Mock
    private GeoLocationProperties geoLocationProperties;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.newBuilder()
                .setVehicleId("test-vehicle")
                .build();

    }

    @Test
    void get_geo_locations_by_vehicle_should_return_geo_location() {
        GeoLocation geoLocation = GeoLocation.newBuilder()
                .setVehicleId("test-vehicle")
                .setLongitude(10.0f)
                .setLatitude(20.0f)
                .setTimeStamp(1234556778)
                .build();

        Mockito.when(geoLocationHandler.getGeoLocation(vehicle)).thenReturn(geoLocation);

        Flux<GeoLocation> result = geoLocationServiceGrpc.getGeoLocationsByVehicle(Mono.just(vehicle));

        StepVerifier.create(result)
                .expectNext(geoLocation)
                .thenCancel()
                .verify();
    }

    @Test
    void get_geo_locations_by_vehicle_should_return_vehicle_not_found_exception_if_vehicle_not_found() {
        Flux<GeoLocation> result = geoLocationServiceGrpc.getGeoLocationsByVehicle(Mono.empty());

        StepVerifier.create(result)
                .expectError(VehicleNotFoundException.class)
                .verify();
    }


    @Test
    void get_geo_locations_by_vehicle_should_return_internal_status_runtime_exception() {
        Mockito.when(geoLocationHandler.getGeoLocation(vehicle)).thenThrow(new InternalStatusRunTimeException());

        Flux<GeoLocation> result = geoLocationServiceGrpc.getGeoLocationsByVehicle(Mono.just(vehicle));

        StepVerifier.create(result)
                .expectError(InternalStatusRunTimeException.class)
                .verify();
    }
}
