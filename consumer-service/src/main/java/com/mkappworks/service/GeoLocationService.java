package com.mkappworks.service;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.ReactorVehicleGeoLocationServiceGrpc;
import com.mkappworks.proto.Vehicle;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GeoLocationService {

    @GrpcClient("producer-service")
    private ReactorVehicleGeoLocationServiceGrpc.ReactorVehicleGeoLocationServiceStub vehicleGeoLocationServiceStub;

    public Flux<GeoLocation> getGeoLocations(Vehicle vehicle) {
        return vehicleGeoLocationServiceStub.getGeoLocationsByVehicle(Mono.just(vehicle));
    }
}