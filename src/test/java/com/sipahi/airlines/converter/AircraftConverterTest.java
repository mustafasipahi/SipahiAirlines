package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AircraftConverterTest {

    @Test
    void shouldConvertToDto() {
        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setExternalId(RandomStringUtils.randomNumeric(3));
        aircraft.setName(RandomStringUtils.randomNumeric(3));
        aircraft.setEconomyRowCount(RandomUtils.nextInt());
        aircraft.setVipRowCount(RandomUtils.nextInt());
        aircraft.setSeatsPerRow(RandomUtils.nextInt());
        AircraftDto response = AircraftConverter.toDto(aircraft);

        assertEquals(aircraft.getExternalId(), response.getExternalId());
        assertEquals(aircraft.getName(), response.getName());
        assertEquals(aircraft.getEconomyRowCount(), response.getEconomyRowCount());
        assertEquals(aircraft.getVipRowCount(), response.getVipRowCount());
        assertEquals(aircraft.getSeatsPerRow(), response.getSeatsPerRow());
    }
}