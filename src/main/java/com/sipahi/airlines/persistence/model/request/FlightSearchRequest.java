package com.sipahi.airlines.persistence.model.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequest {

    private String flightNumber;
}
