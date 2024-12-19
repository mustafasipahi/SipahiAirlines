package com.sipahi.airlines.advice;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import com.sipahi.airlines.advice.error.ErrorResponse;
import com.sipahi.airlines.advice.exception.AirlinesRuntimeException;
import com.sipahi.airlines.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@Slf4j
public class SipahiAirlinesAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("An unknown exception occurred!", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(constructError(ErrorCodes.UNKNOWN_ERROR, e.getMessage()));
    }

    @ExceptionHandler(AirlinesRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(AirlinesRuntimeException e) {
        log.error("Exception occurred!", e);
        return ResponseEntity
                .status(e.getStatus())
                .body(constructError(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleException(BindException e) {
        log.error("An bind exception occurred!", e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        if (CollectionUtils.isNotEmpty(errors)) {
            final String defaultMessage = errors.get(0).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(constructError(ErrorCodes.VALIDATION_ERROR, defaultMessage));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(constructError(ErrorCodes.VALIDATION_ERROR, "Validation error!"));
    }

    private ErrorResponse constructError(final int code, final String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(DateUtil.getTime(LocalDateTime.now()))
                .build();
    }
}