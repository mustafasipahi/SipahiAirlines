package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SeatAvailableFoundException extends AirlinesRuntimeException {

    public SeatAvailableFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.SEAT_NOT_FOUND, "Seat Available Found!");
    }
}
