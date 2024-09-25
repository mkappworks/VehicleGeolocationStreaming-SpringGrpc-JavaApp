package com.mkappworks.service;

import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoLocationSubscriptionInitializer implements CommandLineRunner {

    @GrpcClient("producer-service")
    private VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub locationClient;

    private final GeoLocationListener listener;

    @Override
    public void run(String... args) {
        this.locationClient
                .withWaitForReady()
                .getGeoLocationsByVehicle(Vehicle.newBuilder().setVehicleId("1").build(), listener);
    }
}
