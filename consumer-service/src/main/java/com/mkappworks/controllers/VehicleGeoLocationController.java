package com.mkappworks.controllers;

import com.mkappworks.dtos.VehicleIdsRequest;
import com.mkappworks.service.VehicleGeoLocationConsumerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class VehicleGeoLocationController {

    private final VehicleGeoLocationConsumerService vehicleGeoLocationConsumerService;

    @PostMapping("vehicle/geolocation")
    public ResponseEntity<List<Map<String, List<Map<String, Object>>>>> getGeoLocationsByVehicle(@RequestBody VehicleIdsRequest vehicleIds) {
        return ResponseEntity.ok(vehicleGeoLocationConsumerService.getGeoLocationsByVehicle(vehicleIds.ids()));
    }
}
