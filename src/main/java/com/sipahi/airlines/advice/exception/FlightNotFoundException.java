package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FlightNotFoundException extends AirlinesRuntimeException {

    public FlightNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.FLIGHT_NOT_FOUND, "Flight Not Found!");
    }
}
