package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AircraftNotFoundException extends AirlinesRuntimeException {

    public AircraftNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.AIRCRAFT_NOT_FOUND, "Aircraft Not Found!");
    }
}
