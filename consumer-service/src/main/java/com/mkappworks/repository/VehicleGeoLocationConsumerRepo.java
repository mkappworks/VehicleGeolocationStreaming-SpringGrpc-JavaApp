package com.mkappworks.repository;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocation;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import com.mkappworks.services.config.GeoLocationProperties;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Repository
public class VehicleGeoLocationConsumerRepo implements VehicleGeoLocationConsumerRepoInterface {

    private final VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub asyncClient;
    private final int waitTime;

    public VehicleGeoLocationConsumerRepo(
            VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub asyncClient,
            GeoLocationProperties geoLocationProperties) {
        this.asyncClient = asyncClient;
        this.waitTime = geoLocationProperties.getWaitTime();
    }

    public List<Map<String, List<Map<String, Object>>>> getGeoLocationsByVehicle(List<String> vehicleIds) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        List<Map<String, List<Map<String, Object>>>> response = vehicleIds
                .stream()
                .map(vehicleId -> {
                    final Map<String, List<Map<String, Object>>> vehicleMap = new HashMap<>();
                    vehicleMap.put(vehicleId, new ArrayList<>());
                    return vehicleMap;
                }).toList();

        StreamObserver<Vehicle> responseObserver = asyncClient.getGeoLocationsByVehicle(getStreamObserver(response, countDownLatch));

        vehicleIds
                .forEach(vehicleId ->
                        responseObserver.onNext(
                                Vehicle.newBuilder()
                                        .setVehicleId(vehicleId)
                                        .build()
                        ));


        try {
            boolean await = countDownLatch.await(waitTime, TimeUnit.SECONDS);
            responseObserver.onCompleted();

            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status.
            // Log or handle the interruption in some way
            System.err.println("Thread was interrupted: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private StreamObserver<VehicleGeoLocation> getStreamObserver(List<Map<String, List<Map<String, Object>>>> response, CountDownLatch countDownLatch) {
        return new StreamObserver<>() {
            @Override
            public void onNext(VehicleGeoLocation vehicleGeoLocation) {
                System.out.println("VehicleGeoLocationConsumerService " + vehicleGeoLocation);
                for (Map<String, List<Map<String, Object>>> vehicleMap : response) {
                    var vehicleId = vehicleGeoLocation.getVehicle().getVehicleId();
                    if (vehicleMap.containsKey(vehicleId)) {
                        List<Map<String, Object>> existingList = vehicleMap.get(vehicleId);
                        existingList.add(convertGeoLocationToMap(vehicleGeoLocation.getGeoLocation()));
                        vehicleMap.put(vehicleId, existingList);
                        break;
                    }
                }
            }


            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        };
    }

    private Map<String, Object> convertGeoLocationToMap(GeoLocation geoLocation) {
        Map<String, Object> geoLocationMap = new HashMap<>();
        geoLocationMap.put("latitude", geoLocation.getLatitude());
        geoLocationMap.put("longitude", geoLocation.getLongitude());
        return geoLocationMap;
    }

}


