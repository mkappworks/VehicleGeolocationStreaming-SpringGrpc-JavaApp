package com.mkappworks.services;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocation;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VehicleGeoLocationGeneratorService {
    private final Random random;

    VehicleGeoLocationGeneratorService() {
        random = new Random();
    }

    public VehicleGeoLocation getVehicleGeoLocation(Vehicle vehicle) {
        float latitude = -90 + (90 - (-90)) * random.nextFloat();
        float longitude = -180 + (180 - (-180)) * random.nextFloat();

        GeoLocation geoLocation = GeoLocation.newBuilder()
                .setLongitude(longitude)
                .setLatitude(latitude)
                .build();

        return VehicleGeoLocation.newBuilder()
                .setVehicle(vehicle)
                .setGeoLocation(geoLocation)
                .build();
    }
}
