package com.mkappworks;

import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class VehicleGeoLocationController {

    private final VehicleGeoLocationConsumerService vehicleGeoLocationConsumerService;

    @PostMapping("vehicle/geolocation")
    public List<Map<String, List<Map<Descriptors.FieldDescriptor, Object>>>> getGeoLocationsByVehicle(@RequestBody VehicleIdsRequest vehicleIds) throws InterruptedException {
        System.out.println(vehicleIds.ids());
        return vehicleGeoLocationConsumerService.getGeoLocationsByVehicle(vehicleIds.ids());
    }
}
