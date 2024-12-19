package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.dto.FlightAmountDto;
import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightConverter {

    public static FlightDetailDto toDetailDto(FlightEntity entity) {
        return FlightDetailDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .amount(getAmounts(entity.getAmount()))

                .build();
    }

    public static FlightDto toDto(FlightEntity entity) {
        return FlightDto.builder()
                .flightNumber(entity.getFlightNumber())
                .name(entity.getName())
                .description(entity.getDescription())
                .flightDate(entity.getFlightDate())
                .amount(getAmounts(entity.getAmount()))
                .build();
    }

    private static FlightAmountDto getAmounts(FlightAmountEntity amount) {
        return FlightAmountDto.builder()
                .economy(amount.getEconomy())
                .vip(amount.getVip())
                .build();
    }
}
