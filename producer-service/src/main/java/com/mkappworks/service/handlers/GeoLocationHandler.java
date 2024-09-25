package com.mkappworks.service.handlers;

import com.google.protobuf.Timestamp;
import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

        Instant now = Instant.now();

//        Timestamp timestamp = Timestamp.newBuilder()
//                .setSeconds(now.getEpochSecond())
//                .setNanos(now.getNano())
//                .build();

        return GeoLocation.newBuilder()
                .setLongitude(longitude)
                .setLatitude(latitude)
//                .setTimeStamp(timestamp)
                .build();


    }
}
