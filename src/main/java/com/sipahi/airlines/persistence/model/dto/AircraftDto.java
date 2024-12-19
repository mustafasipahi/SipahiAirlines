package com.sipahi.airlines.persistence.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AircraftDto implements Serializable {

    private static final long serialVersionUID = 8000184504349410869L;

    private String externalId;
    private String name;
    private Integer passengerCount;
}
