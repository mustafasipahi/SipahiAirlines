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

    @NotNull(message = "Flight externalId id cannot be null")
    @NotEmpty(message = "Flight externalId id cannot be empty")
    private String name;

    @NotNull(message = "Flight externalId id cannot be null")
    private Integer passengerCount;
}
