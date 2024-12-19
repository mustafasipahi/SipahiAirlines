package com.sipahi.airlines.persistence.model.request;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class BaseSearchRequest {

    private int page = 0;
    private int size = 20;
    private String sort = "flightDate";
    private Sort.Direction direction = Sort.Direction.DESC;
}
