package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;

@Getter
public class FlightNotUpdatableException extends AirlinesRuntimeException {

    public FlightNotUpdatableException() {
        super(ErrorCodes.FLIGHT_NOT_UPDATABLE, "Flight Not Updatable!");
    }
}
