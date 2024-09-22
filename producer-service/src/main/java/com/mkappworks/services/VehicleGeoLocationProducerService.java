package com.mkappworks.services;

import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import com.mkappworks.proto.VehicleGeoLocation;
import com.mkappworks.proto.VehicleGeoLocationServiceGrpc;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GrpcService
public class VehicleGeoLocationProducerService extends VehicleGeoLocationServiceGrpc.VehicleGeoLocationServiceImplBase {

    private final Random random;
    private final Map<String, List<VehicleGeoLocation>> vehicleGeoLocationMap;
    private final Map<String, StreamObserver<VehicleGeoLocation>> vehicleStreamObservers;
    private ScheduledExecutorService scheduler;

    public VehicleGeoLocationProducerService() {
        this.random = new Random();
        this.vehicleGeoLocationMap = new ConcurrentHashMap<>();
        this.vehicleStreamObservers = new ConcurrentHashMap<>();
    }

    @Override
    public StreamObserver<Vehicle> getGeoLocationsByVehicle(StreamObserver<VehicleGeoLocation> responseObserver) {
        return new StreamObserver<>() {


            @Override
            public void onNext(Vehicle vehicle) {
                var vehicleId = vehicle.getVehicleId();
                System.out.println(vehicleId);
                vehicleStreamObservers.put(vehicleId, responseObserver);
                GeoLocation geoLocation = generateRandomGeoLocation();
                VehicleGeoLocation vehicleGeoLocation =VehicleGeoLocation.newBuilder().setVehicle(vehicle).setGeoLocation(geoLocation).build();
                vehicleGeoLocationMap.computeIfAbsent(vehicleId, k -> new ArrayList<>()).add(vehicleGeoLocation);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in vehicle stream: " + t.getMessage());
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    private GeoLocation generateRandomGeoLocation() {
        float longitude = getRandomLongitude();
        float latitude = getRandomLatitude();
        return GeoLocation.newBuilder()
                .setLongitude(longitude)
                .setLatitude(latitude)
                .build();
    }

    private float getRandomLongitude() {
        return -180 + random.nextFloat() * 360;
    }


    private float getRandomLatitude() {
        return -90 + random.nextFloat() * 180;
    }

    @PostConstruct
    public void startGeoLocationBroadcast() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::broadcastGeoLocations, 0, 2, TimeUnit.SECONDS);
    }

    private void broadcastGeoLocations() {
        vehicleGeoLocationMap.forEach((vehicleId, geoLocations) -> {
            StreamObserver<VehicleGeoLocation> observer = vehicleStreamObservers.get(vehicleId);
            if (observer != null) {
                geoLocations.forEach(observer::onNext);
            }
        });
        vehicleGeoLocationMap.clear();
    }

    @PreDestroy
    public void stopGeoLocationBroadcast() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
