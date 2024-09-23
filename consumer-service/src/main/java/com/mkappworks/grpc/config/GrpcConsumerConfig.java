package com.mkappworks.grpc.config;

import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcConsumerConfig {

    @Value("${consumer-service.vehicle-geo-location.host}")
    private String grpcHost;

    @Value("${consumer-service.vehicle-geo-location.port}")
    private int grpcPort;

    @Bean(name = "managedChannelVehicleGeoLocation")
    public ManagedChannel managedChannelVehicleGeoLocation() {
        return ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .idleTimeout(10, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub vehicleGeoLocationServiceStub(final ManagedChannel vehicleGeoLocationServiceStub) {
        return VehicleGeoLocationServiceGrpc.newStub(vehicleGeoLocationServiceStub);
    }

}
