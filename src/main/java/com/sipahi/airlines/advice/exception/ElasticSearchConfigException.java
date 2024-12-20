package com.sipahi.airlines.advice.exception;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import lombok.Getter;

@Getter
public class ElasticSearchConfigException extends AirlinesRuntimeException {

    public ElasticSearchConfigException(String message) {
        super(ErrorCodes.ELASTIC_SEARCH_ERROR, message);
    }
}
