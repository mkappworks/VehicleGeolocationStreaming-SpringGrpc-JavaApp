package com.mkappworks.repository;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocation;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleGeoLocationConsumerRepoTest {

    @InjectMocks
    private VehicleGeoLocationConsumerRepo vehicleGeoLocationConsumerRepo;

    @Mock
    private VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub asyncClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_get_geo_locations_by_vehicle() {
        // Arrange
        String vehicleId = "vehicle123";
        GeoLocation geoLocation = GeoLocation.newBuilder()
                .setLatitude(40.7128f)
                .setLongitude(-74.0060f)
                .build();

        Vehicle vehicle = Vehicle.newBuilder()
                .setVehicleId(vehicleId)
                .build();

        VehicleGeoLocation vehicleGeoLocation = VehicleGeoLocation.newBuilder()
                .setVehicle(vehicle)
                .setGeoLocation(geoLocation)
                .build();

        StreamObserver<VehicleGeoLocation> responseObserver = mock(StreamObserver.class);
        doAnswer(invocation -> {
            // Simulate receiving vehicleGeoLocation
            responseObserver.onNext(vehicleGeoLocation);
            responseObserver.onCompleted();
            return null;
        }).when(asyncClient).getGeoLocationsByVehicle(any());

        // Act
        List<Map<String, List<Map<String, Object>>>> result = vehicleGeoLocationConsumerRepo.getGeoLocationsByVehicle(Collections.singletonList(vehicleId));

        // Assert
        assertEquals(1, result.size());
        Map<String, List<Map<String, Object>>> vehicleMap = result.getFirst();
        assertTrue(vehicleMap.containsKey(vehicleId));
        assertEquals(1, vehicleMap.get(vehicleId).size());
        Map<String, Object> geoLocationMap = vehicleMap.get(vehicleId).getFirst();
        assertEquals(40.7128f, geoLocationMap.get("latitude"));
        assertEquals(-74.0060f, geoLocationMap.get("longitude"));
    }

    @Test
    public void should_get_empty_response_get_geo_locations_by_vehicle() {
        // Arrange
        StreamObserver<Vehicle> responseObserver = mock(StreamObserver.class);
        doAnswer(invocation -> {
            responseObserver.onError(new RuntimeException("Error occurred"));
            return null;
        }).when(asyncClient).getGeoLocationsByVehicle(any());

        // Act
        List<Map<String, List<Map<String, Object>>>> result = vehicleGeoLocationConsumerRepo.getGeoLocationsByVehicle(Collections.singletonList("vehicle123"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void should_throw_get_geo_locations_by_vehicle() {
        // Arrange
        doAnswer(invocation -> {
            Thread.currentThread().interrupt(); // Simulate interruption
            return null;
        }).when(asyncClient).getGeoLocationsByVehicle(any());

        // Act
        List<Map<String, List<Map<String, Object>>>> result = vehicleGeoLocationConsumerRepo.getGeoLocationsByVehicle(Collections.singletonList("vehicle123"));

        // Assert
        assertTrue(result.isEmpty());
    }
}
