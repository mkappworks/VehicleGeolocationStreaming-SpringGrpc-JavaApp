package com.mkappworks.controller;

import com.mkappworks.dto.GeoLocationResponse;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.service.GeoLocationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final GeoLocationService geoLocationService;

    @GetMapping(value = "/location/{vehicleId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GeoLocationResponse> getStreamBooksByAuthor(@PathVariable String vehicleId) {
        Vehicle vehicle = Vehicle.newBuilder()
                .setVehicleId(vehicleId)
                .build();

        return geoLocationService.getGeoLocations(vehicle).flatMap(geoLocation ->
                Flux.just(
                        GeoLocationResponse.builder()
                                .vehicleId(geoLocation.getVehicleId())
                                .longitude(geoLocation.getLongitude())
                                .latitude(geoLocation.getLatitude())
                                .timestamp(geoLocation.getTimeStamp())
                                .build()
                )
        );
    }
}
