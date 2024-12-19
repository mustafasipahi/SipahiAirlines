package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;

@Getter
public class FlightNotActivateException extends AirlinesRuntimeException {

    public FlightNotActivateException() {
        super(ErrorCodes.FLIGHT_NOT_ACTIVATE, "Flight Not Activate!");
    }
}
