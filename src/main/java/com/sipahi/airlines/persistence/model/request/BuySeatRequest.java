package com.sipahi.airlines.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuySeatRequest {

    private String flightNumber;
    private String seatNo;
}
