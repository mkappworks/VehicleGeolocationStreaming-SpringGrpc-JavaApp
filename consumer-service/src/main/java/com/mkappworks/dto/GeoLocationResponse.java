package com.mkappworks.dto;

import lombok.Builder;

@Builder
public record GeoLocationResponse(
        String vehicleId,
        float latitude,
        float longitude,
        long timestamp) {
}

