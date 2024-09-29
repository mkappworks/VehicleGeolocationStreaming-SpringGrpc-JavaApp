package com.mkappworks.service.handlers;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GeoLocationHandlerTest {

    @Mock
    private Random random;

    @InjectMocks
    private GeoLocationHandler geoLocationHandler;

    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testVehicle = Vehicle.newBuilder()
                .setVehicleId("vehicle123")
                .build();
    }

    @Test
    void getGeoLocation_should_return_valid_geoLocation() {
        Mockito.when(random.nextFloat()).thenReturn(0.5f, 0.75f);

        float expectedLatitude = 0.5f * (90 - (-90)) - 90;
        float expectedLongitude = 0.75f * (180 - (-180)) - 180;

        GeoLocation geoLocation = geoLocationHandler.getGeoLocation(testVehicle);

        LocalDateTime now = LocalDateTime.now();
        long expectedTimestamp = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        assertEquals(testVehicle.getVehicleId(), geoLocation.getVehicleId(), "Vehicle ID should match");
        assertEquals(expectedLatitude, geoLocation.getLatitude(), 0.0001, "Latitude should match expected");
        assertEquals(expectedLongitude, geoLocation.getLongitude(), 0.0001, "Longitude should match expected");
        assertEquals(expectedTimestamp, geoLocation.getTimeStamp(), 1000, "Timestamp should be close to current time");
    }
}
