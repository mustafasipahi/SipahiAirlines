package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AircraftConverter {

    public static AircraftDto toDto(AircraftEntity entity) {
        return AircraftDto.builder()
                .externalId(entity.getExternalId())
                .name(entity.getName())
                .economyRowCount(entity.getEconomyRowCount())
                .vipRowCount(entity.getVipRowCount())
                .seatsPerRow(entity.getSeatsPerRow())
                .build();
    }
}
