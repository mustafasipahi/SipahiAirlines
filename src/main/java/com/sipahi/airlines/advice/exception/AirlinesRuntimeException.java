package com.sipahi.airlines.advice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class AirlinesRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2691698785507525049L;

    private final int code;
    private final HttpStatus status;

    public AirlinesRuntimeException(int code, String message) {
        this(BAD_REQUEST, code, message);
    }

    public AirlinesRuntimeException(HttpStatus status, int code, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
