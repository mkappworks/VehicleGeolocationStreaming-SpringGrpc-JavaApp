package com.mkappworks.controllers;

import com.mkappworks.dtos.VehicleIdsRequest;
import com.mkappworks.services.VehicleGeoLocationConsumerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class VehicleGeoLocationController {

    private final VehicleGeoLocationConsumerService vehicleGeoLocationConsumerService;

    @PostMapping("vehicle/geolocation")
    public List<Map<String, List<Map<String, Object>>>> getGeoLocationsByVehicle(@RequestBody VehicleIdsRequest vehicleIds) {
        return vehicleGeoLocationConsumerService.getGeoLocationsByVehicle(vehicleIds.ids());
    }
}
