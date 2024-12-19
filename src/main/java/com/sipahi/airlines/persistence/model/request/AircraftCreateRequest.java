package com.sipahi.airlines.persistence.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftCreateRequest {

    @NotNull(message = "Flight name cannot be null")
    @NotEmpty(message = "Flight name cannot be empty")
    private String name;

    @NotNull(message = "Flight economyRowCount cannot be null")
    private Integer economyRowCount;

    @NotNull(message = "Flight vipRowCount cannot be null")
    private Integer vipRowCount;

    @NotNull(message = "Flight seatsPerRow cannot be null")
    private Integer seatsPerRow;
}
