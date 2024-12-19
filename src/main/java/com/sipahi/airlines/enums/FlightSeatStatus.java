package com.sipahi.airlines.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FlightSeatStatus {

    AVAILABLE(1),
    SOLD(2),
    CANCELLED(3),
    BLOCKED(4);

    private final int status;

    public static FlightSeatStatus of(int value) {
        return Arrays.stream(FlightSeatStatus.values())
                .filter(status -> status.getStatus() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown flight seat status for value : " + value));
    }
}
