package com.sipahi.airlines.persistence.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipahi.airlines.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightDetailDto implements Serializable {

    private static final long serialVersionUID = 3725646387284733354L;

    private String flightNumber;
    private String name;
    private String description;
    private LocalDateTime flightDate;
    private FlightAmountDto amount;
    private List<FlightSeatDto> seats;
    private FlightStatus status;
}
