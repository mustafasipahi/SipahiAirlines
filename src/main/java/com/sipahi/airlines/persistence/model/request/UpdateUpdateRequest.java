package com.sipahi.airlines.persistence.model.request;

import com.sipahi.airlines.enums.AircraftStatus;
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
public class UpdateUpdateRequest {

    @NotNull(message = "Flight externalId id cannot be null")
    @NotEmpty(message = "Flight externalId id cannot be empty")
    private String externalId;

    private String name;
    private Integer passengerCount;
    private AircraftStatus status;
}
