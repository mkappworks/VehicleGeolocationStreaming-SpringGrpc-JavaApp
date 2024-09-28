package com.mkappworks.service.handlers;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

@Service
public class GeoLocationHandler {
    private final Random random;

    GeoLocationHandler() {
        random = new Random();
    }

    public GeoLocation getGeoLocation(Vehicle vehicle) {
        float latitude = -90 + (90 - (-90)) * random.nextFloat();
        float longitude = -180 + (180 - (-180)) * random.nextFloat();

        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return GeoLocation.newBuilder()
                .setVehicleId(vehicle.getVehicleId())
                .setLongitude(longitude)
                .setLatitude(latitude)
                .setTimeStamp(timestamp)
                .build();


    }
}
