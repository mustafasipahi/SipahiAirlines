package com.sipahi.airlines.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FlightStatus {

    CREATED(1),
    DELETED(2),
    AVAILABLE(3),
    NOT_AVAILABLE(4);

    private final int status;

    public static FlightStatus of(int value) {
        return Arrays.stream(FlightStatus.values())
                .filter(status -> status.getStatus() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown flight status for value : " + value));
    }
}
