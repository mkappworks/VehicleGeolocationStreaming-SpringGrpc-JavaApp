package com.mkappworks.service.config;

import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import com.mkappworks.repository.VehicleGeoLocationConsumerRepo;
import com.mkappworks.repository.VehicleGeoLocationConsumerRepoInterface;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(GeoLocationProperties.class)
public class VehicleGeoLocationConsumerConfig {

    @Bean
    @Primary
    public VehicleGeoLocationConsumerRepoInterface vehicleGeoLocationRepo(
            VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceStub asyncClient,
            GeoLocationProperties geoLocationProperties) {
        return new VehicleGeoLocationConsumerRepo(asyncClient, geoLocationProperties);
    }

//    @Bean
//    @Primary
//    public VehicleGeoLocationConsumerRepoInterface vehicleGeoLocationRepo() {
//        return new VehicleGeoLocationConsumerRepo2();
//    }
}
