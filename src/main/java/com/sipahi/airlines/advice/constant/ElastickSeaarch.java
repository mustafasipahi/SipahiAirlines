package com.sipahi.airlines.advice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElastickSeaarch {

    public static final String FLIGHT_INDEX = "flight";
    public static final String FLIGHT_NUMBER = "flightNumber";
    public static final String FLIGHT_DATE = "flightDate";
    public static final int DEFAULT_SEARCH_SIZE = 20;
}
