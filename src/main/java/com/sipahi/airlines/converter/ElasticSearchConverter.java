package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.model.event.FlightElasticSearchEvent;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchConverter {

    public static FlightElasticSearchEvent toEvent(FlightEntity entity) {
        return FlightElasticSearchEvent.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .status(entity.getStatus())
                .build();
    }
}
