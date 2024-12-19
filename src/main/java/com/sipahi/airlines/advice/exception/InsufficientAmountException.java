package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;

@Getter
public class InsufficientAmountException extends AirlinesRuntimeException {

    public InsufficientAmountException() {
        super(ErrorCodes.INSUFFICIENT_AMOUNT, "Insufficient Amount!");
    }
}
