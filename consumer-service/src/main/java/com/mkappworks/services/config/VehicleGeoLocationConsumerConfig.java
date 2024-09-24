package com.mkappworks.services.config;

import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import com.mkappworks.repository.VehicleGeoLocationConsumerRepo;
import com.mkappworks.repository.VehicleGeoLocationConsumerRepoInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class VehicleGeoLocationConsumerConfig {

    @Bean
    @Primary
    public VehicleGeoLocationConsumerRepoInterface vehicleGeoLocationRepo(VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub asyncClient) {
        return new VehicleGeoLocationConsumerRepo(asyncClient);
    }

//    @Bean
//    @Primary
//    public VehicleGeoLocationConsumerRepoInterface vehicleGeoLocationRepo() {
//        return new VehicleGeoLocationConsumerRepo2();
//    }
}
