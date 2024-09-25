package com.mkappworks.services.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "geo-location")
@Setter
public class GeoLocationProperties {
    private int waitTime;

    public int getWaitTime() {
        return waitTime == 0 ? 2 : waitTime;
    }
}
