package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;

@Getter
public class ElasticEventSaveException extends AirlinesRuntimeException {

    public ElasticEventSaveException(String message) {
        super(ErrorCodes.ELASTIC_SEARCH_SAVE_ERROR, message);
    }
}
