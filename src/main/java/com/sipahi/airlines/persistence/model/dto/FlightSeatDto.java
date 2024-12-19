package com.sipahi.airlines.persistence.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.enums.SeatLocationType;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightSeatDto implements Serializable {

    private static final long serialVersionUID = 6139201047570666621L;

    private String seatNo;
    private FlightSeatStatus status;
    private SeatLocationType location;
    private BigDecimal amount;
}
