package com.mkappworks.config;

import com.mkappworks.service.GeoLocationServiceGrpcImpl;
import com.mkappworks.service.handlers.GeoLocationHandler;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableConfigurationProperties(GeoLocationProperties.class)
public class ServerConfiguration {

    @Bean
    public GrpcServerConfigurer serverConfigurer() {
        return serverBuilder -> serverBuilder.executor(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }

    @Bean
    public GeoLocationServiceGrpcImpl geoLocationServiceGrpcImpl(
            GeoLocationHandler geoLocationHandler,
            ScheduledExecutorService scheduler,
            GeoLocationProperties geoLocationProperties) {
        return new GeoLocationServiceGrpcImpl(geoLocationHandler, scheduler, geoLocationProperties);
    }
}