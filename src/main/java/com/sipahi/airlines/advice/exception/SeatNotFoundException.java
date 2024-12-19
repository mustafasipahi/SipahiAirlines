package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SeatNotFoundException extends AirlinesRuntimeException {

    public SeatNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.SEAT_NOT_FOUND, "Seat Not Found!");
    }
}
