package com.sipahi.airlines.advice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {

    public static final int FLIGHT_NOT_FOUND = 350000;
    public static final int FLIGHT_ALREADY_EXIST = 350001;
    public static final int AIRCRAFT_NOT_FOUND = 350002;
    public static final int FLIGHT_NOT_UPDATABLE = 350003;
    public static final int FLIGHT_NOT_ACTIVATE = 350004;
    public static final int SEAT_NOT_FOUND = 350005;
    public static final int ACCOUNT_NOT_FOUND = 350006;
    public static final int INSUFFICIENT_AMOUNT = 350007;
    public static final int FLIGHT_AMOUNT_NOT_FOUND = 350008;
    public static final int ELASTIC_SEARCH_ERROR = 350009;
    public static final int ELASTIC_SEARCH_SAVE_ERROR = 350010;

    public static final int VALIDATION_ERROR = 350998;
    public static final int UNKNOWN_ERROR = 350999;
}
