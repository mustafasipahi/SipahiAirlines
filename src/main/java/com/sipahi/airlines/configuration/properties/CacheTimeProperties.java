package com.sipahi.airlines.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cache-time")
public class CacheTimeProperties {

    private Long flightDetailCacheMinute;
    private Long aircraftDetailCacheMinute;
    private Long aircraftListCacheMinute;
}
