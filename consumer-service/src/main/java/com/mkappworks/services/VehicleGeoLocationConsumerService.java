package com.mkappworks.services;

import com.google.protobuf.Descriptors;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocation;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class VehicleGeoLocationConsumerService {

    @GrpcClient("grpc-service")
    VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub asyncClient;

    public List<Map<String, List<Map<Descriptors.FieldDescriptor, Object>>>> getGeoLocationsByVehicle(List<String> vehicleIds) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        List<Map<String, List<Map<Descriptors.FieldDescriptor, Object>>>> response = vehicleIds
                .stream()
                .map(vehicleId -> {
                    final Map<String, List<Map<Descriptors.FieldDescriptor, Object>>> vehicleMap = new HashMap<>();
                    vehicleMap.put(vehicleId, new ArrayList<>());
                    return vehicleMap;
                }).toList();

        StreamObserver<Vehicle> responseObserver = asyncClient.getGeoLocationsByVehicle(
                new StreamObserver<>() {
                    @Override
                    public void onNext(VehicleGeoLocation vehicleGeoLocation) {
                        System.out.println("VehicleGeoLocationConsumerService " + vehicleGeoLocation);
                        for (Map<String, List<Map<Descriptors.FieldDescriptor, Object>>> vehicleMap : response) {
                            var vehicleId = vehicleGeoLocation.getVehicle().getVehicleId();
                            if (vehicleMap.containsKey(vehicleId)) {
                                List<Map<Descriptors.FieldDescriptor, Object>> existingList = vehicleMap.get(vehicleId);
                                existingList.add(vehicleGeoLocation.getGeoLocation().getAllFields());
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
                }
        );


        vehicleIds
                .forEach(vehicleId ->
                        responseObserver.onNext(
                                Vehicle.newBuilder()
                                        .setVehicleId(vehicleId)
                                        .build()
                        ));

        responseObserver.onCompleted();

        boolean await = countDownLatch.await(10, TimeUnit.SECONDS);
        return await ? response : Collections.emptyList();
    }
}
