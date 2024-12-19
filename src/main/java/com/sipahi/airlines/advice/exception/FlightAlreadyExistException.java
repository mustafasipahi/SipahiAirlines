package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FlightAlreadyExistException extends AirlinesRuntimeException {

    public FlightAlreadyExistException() {
        super(HttpStatus.CONFLICT, ErrorCodes.FLIGHT_ALREADY_EXIST, "Flight Already Exist!");
    }
}
