package com.mkappworks.grpc.config;


import com.mkappworks.grpc.VehicleGeoLocationServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {

    @Value("${grpc.server.port}")
    private int port;

    private final VehicleGeoLocationServiceImpl vehicleGeoLocationService;

    @Bean
    public Server grpcServer() throws IOException {
        return ServerBuilder.forPort(port)
                .addService(vehicleGeoLocationService)
                .build()
                .start();
    }
}
