package com.mkappworks.service;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.ReactorVehicleGeoLocationServiceGrpc;
import com.mkappworks.proto.Vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class GeoLocationServiceTest {

    @Mock
    private ReactorVehicleGeoLocationServiceGrpc.ReactorVehicleGeoLocationServiceStub vehicleGeoLocationServiceStub;

    @InjectMocks
    private GeoLocationService geoLocationService;

    private Vehicle testVehicle;
    private GeoLocation geoLocation1;
    private GeoLocation geoLocation2;

    @BeforeEach
    void setUp() {
        testVehicle = Vehicle.newBuilder()
                .setVehicleId("vehicle123")
                .build();

        geoLocation1 = GeoLocation.newBuilder()
                .setVehicleId("vehicle123")
                .setLongitude(12.34f)
                .setLatitude(56.78f)
                .setTimeStamp(1727629161)
                .build();

        geoLocation2 = GeoLocation.newBuilder()
                .setVehicleId("vehicle123")
                .setLongitude(98.76f)
                .setLatitude(54.32f)
                .setTimeStamp(1827629161)
                .build();
    }

    @Test
    void getGeoLocations_should_return_flux_of_geo_locations() {
        Mockito.when(vehicleGeoLocationServiceStub.getGeoLocationsByVehicle(any(Mono.class)))
                .thenReturn(Flux.just(geoLocation1, geoLocation2));

        Flux<GeoLocation> geoLocationFlux = geoLocationService.getGeoLocations(testVehicle);

        // Use StepVerifier to verify the Flux
        StepVerifier.create(geoLocationFlux)
                .expectNext(geoLocation1)
                .expectNext(geoLocation2)
                .verifyComplete();
    }

    @Test
    void getGeoLocations_should_return_empty_flux_if_no_geo_locations() {
        Mockito.when(vehicleGeoLocationServiceStub.getGeoLocationsByVehicle(any(Mono.class)))
                .thenReturn(Flux.empty());

        Flux<GeoLocation> geoLocationFlux = geoLocationService.getGeoLocations(testVehicle);

        // Verify that the Flux is empty
        StepVerifier.create(geoLocationFlux)
                .expectNextCount(0)
                .verifyComplete();
    }
}
