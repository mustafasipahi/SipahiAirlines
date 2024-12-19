package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightConverter {

    public static FlightDto toDto(FlightEntity entity) {
        return FlightDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .flightDate(entity.getFlightDate())
                .build();
    }
}
