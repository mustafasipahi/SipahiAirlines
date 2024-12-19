package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.model.dto.FlightAmountDto;
import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.service.FlightSeatService;
import com.sipahi.airlines.util.FlightSeatUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FlightConverter {

    private final FlightSeatService flightSeatService;

    public FlightDetailDto toDetailDto(FlightEntity entity) {
        return FlightDetailDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .seats(getSeats(entity))
                .build();
    }

    public FlightDto toDto(FlightEntity entity) {
        return FlightDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .amount(getAmounts(entity.getAmount()))
                .build();
    }

    private FlightAmountDto getAmounts(FlightAmountEntity amount) {
        return FlightAmountDto.builder()
                .economy(amount.getEconomy())
                .vip(amount.getVip())
                .build();
    }

    private List<FlightSeatDto> getSeats(FlightEntity entity) {
        List<FlightSeatDocument> flightSeats = flightSeatService.getFlightSeats(entity.getId());
        return FlightSeatUtil.getAvailableSeats(entity, flightSeats);
    }
}
