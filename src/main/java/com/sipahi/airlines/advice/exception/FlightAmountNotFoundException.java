package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FlightAmountNotFoundException extends AirlinesRuntimeException {

    public FlightAmountNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.FLIGHT_AMOUNT_NOT_FOUND, "Flight Amount Not Found!");
    }
}
