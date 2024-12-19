package com.sipahi.airlines.persistence.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto implements Serializable {

    private static final long serialVersionUID = 8010945412154491278L;

    private String flightNumber;
    private String name;
    private LocalDateTime flightDate;
}
