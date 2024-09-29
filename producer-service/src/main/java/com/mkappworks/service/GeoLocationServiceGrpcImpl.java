package com.mkappworks.service;

import com.mkappworks.config.GeoLocationProperties;
import com.mkappworks.exceptions.InternalStatusRunTimeException;
import com.mkappworks.exceptions.VehicleNotFoundException;
import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.ReactorVehicleGeoLocationServiceGrpc;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.service.handlers.GeoLocationHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GeoLocationServiceGrpcImpl extends ReactorVehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceImplBase {

    private final GeoLocationHandler geoLocationHandler;
    private final ScheduledExecutorService scheduler;
    private final GeoLocationProperties geoLocationProperties;

    @Override
    public Flux<GeoLocation> getGeoLocationsByVehicle(Mono<Vehicle> request) {
        return request.flatMapMany(vehicle -> {
            // Simulate periodic location updates every waitTime seconds using Flux.interval
            return Flux.interval(Duration.ofSeconds(geoLocationProperties.getWaitTime()))
                    .flatMap(tick -> {
                        try {
                            GeoLocation geoLocation = geoLocationHandler.getGeoLocation(vehicle);
                            return Mono.just(geoLocation);
                        } catch (Exception e) {
                            // Log the error and return a Mono error to propagate it downstream
                            log.error("Error fetching GeoLocation", e);
                            return Mono.error(new InternalStatusRunTimeException());
                        }
                    })
                    .onErrorResume(e -> {
                        // Handle any errors and shutdown scheduler or provide fallback logic
                        log.error("Error occurred: ", e);
                        scheduler.shutdown();
                        return Flux.error(e); // Propagate error to the client
                    });
        }).switchIfEmpty(Flux.error(new VehicleNotFoundException()));
    }
}
