package com.mkappworks.services;

import com.mkappworks.repository.VehicleGeoLocationConsumerRepoInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VehicleGeoLocationConsumerService {

    private final VehicleGeoLocationConsumerRepoInterface vehicleGeoLocationConsumerRepo;

    public List<Map<String, List<Map<String, Object>>>> getGeoLocationsByVehicle(List<String> vehicleIds) {
        return vehicleGeoLocationConsumerRepo.getGeoLocationsByVehicle(vehicleIds);
    }

}
