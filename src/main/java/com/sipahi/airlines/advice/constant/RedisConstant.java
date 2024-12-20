package com.sipahi.airlines.advice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

    public static final String FLIGHT_DETAIL = "FlightDetail_";
    public static final String AIRCRAFT_DETAIL_BY_ID = "AircraftDetailById_";
    public static final String AIRCRAFT_DETAIL_BY_EXTERNAL_ID = "AircraftDetailByExternalId_";
    public static final String AIRCRAFT_LIST = "AircraftList_";
}
