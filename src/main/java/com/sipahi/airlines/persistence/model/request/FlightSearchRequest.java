package com.sipahi.airlines.persistence.model.request;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequest extends BaseSearchRequest {

    private String flightName;
    private String flightNumber;
}
