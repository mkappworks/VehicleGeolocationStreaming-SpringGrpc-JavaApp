package com.mkappworks.service;

import com.mkappworks.exceptions.NoGeoLocationException;
import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import com.mkappworks.service.handlers.GeoLocationHandler;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GrpcService
public class VehicleGeoLocationServiceImpl extends VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceImplBase {

    private final GeoLocationHandler geoLocationHandler;
    private final ScheduledExecutorService scheduler;

    VehicleGeoLocationServiceImpl(GeoLocationHandler geoLocationHandler) {
        this.geoLocationHandler = geoLocationHandler;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void getGeoLocationsByVehicle(Vehicle vehicle, StreamObserver<GeoLocation> responseObserver) {
        try {
            // Simulate periodic location updates for each vehicle
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    GeoLocation geoLocation = geoLocationHandler.getGeoLocation(vehicle);
                    if (geoLocation == null) {
                        responseObserver.onError(new NoGeoLocationException());
                    } else {
                        responseObserver.onNext(geoLocation);
                    }
                } catch (Exception e) {
                    // Log and propagate any internal exceptions
                    e.printStackTrace();
                    scheduler.shutdown();
                    responseObserver.onError(Status.INTERNAL.withDescription("Internal server error").withCause(e).asRuntimeException());
                    responseObserver.onCompleted();
                }
            }, 0, 2, TimeUnit.SECONDS);

        } catch (Exception e) {
            // Log the error and return it to the client
            e.printStackTrace();
            scheduler.shutdown();
            responseObserver.onError(Status.UNKNOWN.withDescription("An unknown error occurred").withCause(e).asRuntimeException());
            responseObserver.onCompleted();
        }
    }
}
