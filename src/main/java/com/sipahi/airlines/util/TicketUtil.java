package com.sipahi.airlines.util;

import joptsimple.internal.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketUtil {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateTicketNo(String flightNumber, String seatNumber) {
        String randomCode = generateRandomCode();
        return "T" + "-" + randomCode + "-" + seatNumber + "-" + minFlightNumber(flightNumber);
    }

    private static String generateRandomCode() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = RANDOM.nextInt(CHARS.length());
            result.append(CHARS.charAt(index));
        }
        return result.toString();
    }

    public static String minFlightNumber(String flightNumber) {
        if (StringUtils.isBlank(flightNumber)) {
            return Strings.EMPTY;
        }
        int length = flightNumber.length();
        if (length >= 5) {
            return flightNumber.substring(0, 5);
        } else if (length == 4) {
            return flightNumber.substring(0, 4);
        } else if (length == 3) {
            return flightNumber.substring(0, 3);
        }
        return flightNumber;
    }
}
