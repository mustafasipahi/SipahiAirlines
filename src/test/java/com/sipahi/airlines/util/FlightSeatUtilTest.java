package com.sipahi.airlines.util;

import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.enums.SeatLocationType;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FlightSeatUtilTest {

    @Test
    void shouldGetAvailableSeatsWhenAllCase() {
        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setSeatsPerRow(4);
        aircraft.setVipRowCount(1);
        aircraft.setEconomyRowCount(2);
        FlightAmountEntity amount = new FlightAmountEntity();
        amount.setVip(new BigDecimal("100"));
        amount.setEconomy(new BigDecimal("50"));
        List<FlightSeatDto> availableSeats = FlightSeatUtil.getAvailableSeats(aircraft, amount, List.of());

        assertEquals(12, availableSeats.size());

        assertEquals("V-1A", availableSeats.get(0).getSeatNo());
        assertEquals(FlightSeatStatus.AVAILABLE, availableSeats.get(0).getStatus());
        assertEquals(SeatLocationType.WINDOW, availableSeats.get(0).getLocation());
        assertEquals(new BigDecimal("100"), availableSeats.get(0).getAmount());

        assertEquals("1B", availableSeats.get(5).getSeatNo());
        assertEquals(FlightSeatStatus.AVAILABLE, availableSeats.get(5).getStatus());
        assertEquals(SeatLocationType.AISLE, availableSeats.get(5).getLocation());
        assertEquals(new BigDecimal("50"), availableSeats.get(5).getAmount());
    }

    @Test
    void shouldOnlyAvailableSeatsWhenAllCase() {
        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setSeatsPerRow(4);
        aircraft.setVipRowCount(1);
        aircraft.setEconomyRowCount(2);
        FlightAmountEntity amount = new FlightAmountEntity();
        amount.setVip(new BigDecimal("100"));
        amount.setEconomy(new BigDecimal("50"));
        FlightSeatDocument flightSeatDocument1 = new FlightSeatDocument();
        flightSeatDocument1.setSeatNo("V-1B");
        flightSeatDocument1.setFlightSeatStatus(FlightSeatStatus.BLOCKED);
        FlightSeatDocument flightSeatDocument2 = new FlightSeatDocument();
        flightSeatDocument2.setSeatNo("2D");
        flightSeatDocument2.setFlightSeatStatus(FlightSeatStatus.SOLD);
        List<FlightSeatDocument> flightSeatDocuments = List.of(flightSeatDocument1, flightSeatDocument2);
        List<FlightSeatDto> availableSeats = FlightSeatUtil.getAvailableSeats(aircraft, amount, flightSeatDocuments);

        assertEquals("V-1A", availableSeats.get(0).getSeatNo());
        assertEquals(FlightSeatStatus.AVAILABLE, availableSeats.get(0).getStatus());
        assertEquals(SeatLocationType.WINDOW, availableSeats.get(0).getLocation());
        assertEquals(new BigDecimal("100"), availableSeats.get(0).getAmount());

        assertEquals("V-1B", availableSeats.get(1).getSeatNo());
        assertEquals(FlightSeatStatus.BLOCKED, availableSeats.get(1).getStatus());
        assertEquals(SeatLocationType.AISLE, availableSeats.get(1).getLocation());
        assertEquals(new BigDecimal("100"), availableSeats.get(1).getAmount());

        assertEquals("1B", availableSeats.get(5).getSeatNo());
        assertEquals(FlightSeatStatus.AVAILABLE, availableSeats.get(5).getStatus());
        assertEquals(SeatLocationType.AISLE, availableSeats.get(5).getLocation());
        assertEquals(new BigDecimal("50"), availableSeats.get(5).getAmount());

        assertEquals("2D", availableSeats.get(11).getSeatNo());
        assertEquals(FlightSeatStatus.SOLD, availableSeats.get(11).getStatus());
        assertEquals(SeatLocationType.WINDOW, availableSeats.get(11).getLocation());
        assertEquals(new BigDecimal("50"), availableSeats.get(11).getAmount());
    }
}