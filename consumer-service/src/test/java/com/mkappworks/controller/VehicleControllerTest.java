package com.mkappworks.controller;

import com.mkappworks.dto.GeoLocationResponse;
import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.service.GeoLocationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GeoLocationService geoLocationService;

    private static final String VEHICLE_ID = "vehicle123";
    private static final float LONGITUDE = 50.1f;
    private static final float LATITUDE = 20.2f;
    private static final long TIMESTAMP = 172762916;

    @BeforeEach
    void setUp() {
        GeoLocation mockGeoLocation = GeoLocation.newBuilder()
                .setVehicleId(VEHICLE_ID)
                .setLongitude(LONGITUDE)
                .setLatitude(LATITUDE)
                .setTimeStamp(TIMESTAMP)
                .build();
        Flux<GeoLocation> geoLocationFlux = Flux.just(mockGeoLocation);

        Mockito.when(geoLocationService.getGeoLocations(any(Vehicle.class)))
                .thenReturn(geoLocationFlux);
    }

    @Test
    void get_stream_books_by_author_returns_geo_location_responses() {
        webTestClient.get()
                .uri("/api/v1/vehicle/location/{vehicleId}", VEHICLE_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM_VALUE)
                .expectBodyList(GeoLocationResponse.class)
                .hasSize(1)
                .consumeWith(response -> {
                    GeoLocationResponse geoLocationResponse = Objects.requireNonNull(response.getResponseBody()).getFirst();
                    assert geoLocationResponse.vehicleId().equals(VEHICLE_ID);
                    assert geoLocationResponse.longitude() == LONGITUDE;
                    assert geoLocationResponse.latitude() == LATITUDE;
                    assert geoLocationResponse.timestamp() == TIMESTAMP;
                });
    }
}
