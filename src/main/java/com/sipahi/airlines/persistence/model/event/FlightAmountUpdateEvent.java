package com.sipahi.airlines.persistence.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightAmountUpdateEvent implements Serializable {

    private BigDecimal vipAmount;
    private BigDecimal economyAmount;
    private Long flightId;
}
