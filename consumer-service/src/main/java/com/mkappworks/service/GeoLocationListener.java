package com.mkappworks.service;

import com.mkappworks.dto.GeoLocationResponse;
import com.mkappworks.proto.GeoLocation;
import com.mkappworks.proto.Vehicle;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class GeoLocationListener implements StreamObserver<GeoLocation> {

    private static final Logger log = LoggerFactory.getLogger(GeoLocationListener.class);
    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());
    private final long sseTimeout;

    @Getter
    private Vehicle vehicle;

    public GeoLocationListener(@Value("${sse.timeout:300000}") long sseTimeout) {
        this.sseTimeout = sseTimeout;
    }

    public SseEmitter createEmitter(Vehicle vehicle) {
        this.vehicle = vehicle;
        var emitter = new SseEmitter(this.sseTimeout);
        this.emitters.add(emitter);
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        emitter.onError(ex -> this.emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void onNext(GeoLocation geoLocation) {
        var dto = GeoLocationResponse
                .builder()
                .latitude(geoLocation.getLatitude())
                .longitude(geoLocation.getLongitude())
//                .timestamp(geoLocation.getTimeStamp())
                .build();
        this.emitters.removeIf(e -> !this.send(e, dto));
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("streaming error", throwable);
        this.emitters.forEach(e -> e.completeWithError(throwable));
        this.emitters.clear();
    }

    @Override
    public void onCompleted() {
        this.emitters.forEach(ResponseBodyEmitter::complete);
        this.emitters.clear();
    }

    private boolean send(SseEmitter emitter, Object o) {
        try {
            emitter.send(o);
            return true;
        } catch (IOException e) {
            log.warn("sse error {}", e.getMessage());
            return false;
        }
    }

}
