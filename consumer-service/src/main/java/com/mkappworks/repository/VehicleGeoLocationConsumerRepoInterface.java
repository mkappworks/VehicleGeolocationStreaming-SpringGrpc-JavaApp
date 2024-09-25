package com.mkappworks.repository;

import java.util.List;
import java.util.Map;

public interface VehicleGeoLocationConsumerRepoInterface {
    List<Map<String, List<Map<String, Object>>>> getGeoLocationsByVehicle(List<String> vehicleIds);
}
