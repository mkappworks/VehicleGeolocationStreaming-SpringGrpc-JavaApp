package com.mkappworks.service;

import com.mkappworks.exceptions.StreamObserverException;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocation;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import com.mkappworks.service.handlers.VehicleGeoLocationGeneratorHandler;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GrpcService
public class VehicleGeoLocationServiceImpl extends VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceImplBase {

    private final VehicleGeoLocationGeneratorHandler vehicleGeoLocationGeneratorService;
    private final ScheduledExecutorService scheduler;

    VehicleGeoLocationServiceImpl(VehicleGeoLocationGeneratorHandler grpcVehicleGeoLocationService) {
        this.vehicleGeoLocationGeneratorService = grpcVehicleGeoLocationService;
        scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public StreamObserver<Vehicle> getGeoLocationsByVehicle(StreamObserver<VehicleGeoLocation> responseObserver) {

        return new StreamObserver<>() {
            @Override
            public void onNext(Vehicle vehicle) {

                // Simulate periodic location updates for each vehicle
                scheduler.scheduleAtFixedRate(() -> {
                    VehicleGeoLocation vehicleGeoLocation = vehicleGeoLocationGeneratorService.getVehicleGeoLocation(vehicle);
                    System.out.println("VehicleGeoLocationConsumerService  vehicleGeoLocation" + vehicleGeoLocation);

                    responseObserver.onNext(vehicleGeoLocation);

                }, 0, 2, TimeUnit.SECONDS);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in vehicle stream: " + t.getMessage());
                responseObserver.onError(t);
                throw (new StreamObserverException(t));
            }

            @Override
            public void onCompleted() {
                System.out.println("VehicleGeoLocationConsumerService onCompleted");
                scheduler.shutdown();
                responseObserver.onCompleted();
            }
        };
    }

}