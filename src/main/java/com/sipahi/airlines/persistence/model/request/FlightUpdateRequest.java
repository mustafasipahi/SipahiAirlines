package com.sipahi.airlines.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightUpdateRequest {

    @NotNull(message = "Flight name cannot be null")
    @NotEmpty(message = "Flight name cannot be empty")
    private String flightNumber;

    private String name;
    private String description;
    private String aircraftId;
    private LocalDateTime flightDate;
    private BigDecimal vipAmount;
    private BigDecimal economyAmount;
}
