package com.mkappworks.controller;

import com.mkappworks.proto.Vehicle;
import com.mkappworks.service.GeoLocationListener;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final GeoLocationListener listener;

    @GetMapping(value = "location/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getLocations(@PathVariable String id) {
        return listener.createEmitter(Vehicle.newBuilder().setVehicleId(id).build());
    }
}
