package com.sipahi.airlines.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightCreateRequest {

    @NotNull(message = "Flight name cannot be null")
    @NotEmpty(message = "Flight name cannot be empty")
    private String name;

    @NotNull(message = "Flight description cannot be null")
    @NotEmpty(message = "Flight description cannot be empty")
    private String description;

    @NotNull(message = "Flight aircraft id cannot be null")
    @NotEmpty(message = "Flight aircraft id cannot be empty")
    private String aircraftId;

    @NotNull(message = "Flight flight date cannot be null")
    private LocalDateTime flightDate;

    @NotNull(message = "Flight flight date cannot be null")
    private BigDecimal vipAmount;

    @NotNull(message = "Flight flight date cannot be null")
    private BigDecimal economyAmount;
}
