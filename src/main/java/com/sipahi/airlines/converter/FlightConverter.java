package com.sipahi.airlines.converter;

import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.service.AircraftService;
import com.sipahi.airlines.service.FlightAmountService;
import com.sipahi.airlines.service.FlightSeatService;
import com.sipahi.airlines.util.FlightSeatUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class FlightConverter {

    private final FlightSeatService flightSeatService;
    private final FlightAmountService flightAmountService;
    private final AircraftService aircraftService;

    public FlightDetailDto toDetailDto(FlightEntity entity) {
        return FlightDetailDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .seats(getSeats(entity))
                .status(entity.getStatus())
                .build();
    }

    public FlightDto constructDto(Map<String, Object> elasticResultMap) {
        try {
            return FlightDto.builder()
                    .flightNumber((String) elasticResultMap.get("flightNumber"))
                    .name((String) elasticResultMap.get("name"))
                    .description((String) elasticResultMap.get("description"))
                    .status(elasticResultMap.containsKey("status") ?
                            FlightStatus.valueOf((String) elasticResultMap.get("status")) : null)
                    .build();
        } catch (Exception e) {
            log.error("Error while constructing FlightDto from result map: {}", elasticResultMap, e);
            throw new IllegalStateException("Failed to construct FlightDto", e);
        }
    }

    public List<FlightSeatDto> toSeats(FlightEntity entity) {
        return getSeats(entity);
    }

    private List<FlightSeatDto> getSeats(FlightEntity entity) {
        AircraftEntity aircraft = aircraftService.getDetailById(entity.getAircraftId());
        FlightAmountEntity flightAmount = flightAmountService.findByFlightId(entity.getId());
        List<FlightSeatDocument> flightSeats = flightSeatService.getFlightSeats(entity.getId());
        return FlightSeatUtil.getAvailableSeats(aircraft, flightAmount, flightSeats);
    }
}
