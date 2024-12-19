package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountNotFoundException extends AirlinesRuntimeException {

    public AccountNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.ACCOUNT_NOT_FOUND, "Account Not Found!");
    }
}
