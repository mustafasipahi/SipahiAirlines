package com.sipahi.airlines.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AircraftStatus {

    AVAILABLE(1),
    NOT_AVAILABLE(2);

    private final int status;

    public static AircraftStatus of(int value) {
        return Arrays.stream(AircraftStatus.values())
                .filter(status -> status.getStatus() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown aircraft status for value : " + value));
    }
}
