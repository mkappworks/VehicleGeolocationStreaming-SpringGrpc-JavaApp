package com.mkappworks.dto;

import com.google.protobuf.Timestamp;
import lombok.Builder;

@Builder
public record GeoLocationResponse(
        float latitude,
        float longitude,
        Timestamp timestamp) {
}

