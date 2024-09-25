package com.mkappworks.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class VehicleGeoLocationConsumerRepo2 implements VehicleGeoLocationConsumerRepoInterface {

    public List<Map<String, List<Map<String, Object>>>> getGeoLocationsByVehicle(List<String> vehicleIds) {

        System.out.println("getGeoLocationsByVehicle 222");

        // Call Other service
        // map and return result;

        return Collections.emptyList();
    }
}
