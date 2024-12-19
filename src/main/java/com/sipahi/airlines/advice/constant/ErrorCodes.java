package com.sipahi.airlines.advice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {

    public static final int FLIGHT_NOT_FOUND = 350000;
    public static final int FLIGHT_ALREADY_EXIST = 350001;
    public static final int AIRCRAFT_NOT_FOUND = 350002;

    public static final int VALIDATION_ERROR = 350998;
    public static final int UNKNOWN_ERROR = 350999;
}
