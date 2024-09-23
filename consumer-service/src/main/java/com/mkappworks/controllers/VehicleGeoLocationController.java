package com.mkappworks.controllers;

import com.google.protobuf.Descriptors;
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
    public List<Map<String, List<Map<Descriptors.FieldDescriptor, Object>>>> getGeoLocationsByVehicle(@RequestBody VehicleIdsRequest vehicleIds) throws InterruptedException {
        return vehicleGeoLocationConsumerService.getGeoLocationsByVehicle(vehicleIds.ids());
    }
}
