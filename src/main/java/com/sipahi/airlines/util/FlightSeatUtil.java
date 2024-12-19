package com.sipahi.airlines.util;

import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.enums.SeatLocationType;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightSeatUtil {

    public static List<FlightSeatDto> getAvailableSeats(FlightEntity entity, List<FlightSeatDocument> flightSeats) {
        AircraftEntity aircraft = entity.getAircraft();
        FlightAmountEntity amount = entity.getAmount();
        String[] seatLetters = getSeatName(aircraft.getSeatsPerRow());
        List<FlightSeatDto> seats = new ArrayList<>();
        addVipSeats(aircraft, amount, seats, seatLetters, flightSeats);
        addEconomySeats(aircraft, amount, seats, seatLetters, flightSeats);
        return seats;
    }

    private static String[] getSeatName(int seatsPerRow) {
        String[] seatLetters = new String[seatsPerRow];
        for (int i = 0; i < seatsPerRow; i++) {
            seatLetters[i] = String.valueOf((char) ('A' + i));
        }
        return seatLetters;
    }

    private static void addVipSeats(AircraftEntity aircraft,
                                    FlightAmountEntity amount,
                                    List<FlightSeatDto> seats,
                                    String[] seatLetters,
                                    List<FlightSeatDocument> flightSeats) {
        for (int row = 1; row <= aircraft.getVipRowCount(); row++) {
            for (String seatLetter : seatLetters) {
                String seatNo = "V-" + row + seatLetter;
                FlightSeatStatus seatStatus = getSeatStatus(seatNo, flightSeats);
                SeatLocationType seatType = getSeatLocation(seatLetter, aircraft.getSeatsPerRow());
                seats.add(FlightSeatDto.builder()
                        .seatNo(seatNo)
                        .status(seatStatus)
                        .location(seatType)
                        .amount(amount.getVip())
                        .build());
            }
        }
    }

    private static void addEconomySeats(AircraftEntity aircraft,
                                        FlightAmountEntity amount,
                                        List<FlightSeatDto> seats,
                                        String[] seatLetters,
                                        List<FlightSeatDocument> flightSeats) {
        for (int row = 1; row <= aircraft.getEconomyRowCount(); row++) {
            for (String seatLetter : seatLetters) {
                String seatNo = row + seatLetter;
                FlightSeatStatus seatStatus = getSeatStatus(seatNo, flightSeats);
                SeatLocationType seatType = getSeatLocation(seatLetter, aircraft.getSeatsPerRow());
                seats.add(FlightSeatDto.builder()
                        .seatNo(seatNo)
                        .status(seatStatus)
                        .location(seatType)
                        .amount(amount.getEconomy())
                        .build());
            }
        }
    }

    private static FlightSeatStatus getSeatStatus(String seatNo, List<FlightSeatDocument> flightSeats) {
        if (CollectionUtils.isEmpty(flightSeats)) {
            return FlightSeatStatus.AVAILABLE;
        }
        for (FlightSeatDocument flightSeat : flightSeats) {
            if (flightSeat.getSeatNo().equalsIgnoreCase(seatNo)) {
                return flightSeat.getFlightSeatStatus();
            }
        }
        return FlightSeatStatus.AVAILABLE;
    }

    private static SeatLocationType getSeatLocation(String seatLetter, int seatsPerRow) {
        if (seatsPerRow == 6) {
            if (seatLetter.equals("A") || seatLetter.equals("F")) {
                return SeatLocationType.WINDOW;
            } else if (seatLetter.equals("B") || seatLetter.equals("E")) {
                return SeatLocationType.MIDDLE;
            } else {
                return SeatLocationType.AISLE;
            }
        } else if (seatsPerRow == 4) {
            if (seatLetter.equals("A") || seatLetter.equals("D")) {
                return SeatLocationType.WINDOW;
            } else {
                return SeatLocationType.AISLE;
            }
        }
        return SeatLocationType.UNKNOWN;
    }
}
