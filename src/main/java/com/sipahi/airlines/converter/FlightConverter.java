package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.model.dto.FlightAmountDto;
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
import org.springframework.stereotype.Component;

import java.util.List;

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

    public FlightDto toDto(FlightEntity entity) {
        return FlightDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .amount(getAmounts(entity.getId()))
                .status(entity.getStatus())
                .build();
    }

    private FlightAmountDto getAmounts(Long flightId) {
        FlightAmountEntity flightAmount = flightAmountService.findByFlightId(flightId);
        return FlightAmountDto.builder()
                .economy(flightAmount.getEconomy())
                .vip(flightAmount.getVip())
                .build();
    }

    private List<FlightSeatDto> getSeats(FlightEntity entity) {
        AircraftEntity aircraft = aircraftService.getDetailById(entity.getAircraftId());
        FlightAmountEntity flightAmount = flightAmountService.findByFlightId(entity.getId());
        List<FlightSeatDocument> flightSeats = flightSeatService.getFlightSeats(entity.getId());
        return FlightSeatUtil.getAvailableSeats(aircraft, flightAmount, flightSeats);
    }
}
