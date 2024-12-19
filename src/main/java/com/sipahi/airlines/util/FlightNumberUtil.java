package com.sipahi.airlines.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightNumberUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();

    public static String generateFlightNumber() {
        LocalDateTime now = LocalDateTime.now();
        String dateTimePart = now.format(FORMATTER);
        int randomPart = RANDOM.nextInt(1000);
        String formattedRandomPart = String.format("%03d", randomPart);
        return "FL-" + dateTimePart + "-" + formattedRandomPart;
    }
}
