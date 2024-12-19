package com.sipahi.airlines.service;

import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mongo.repository.FlightSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSeatService {

    private final FlightSeatRepository flightSeatRepository;

    @Transactional
    public void saveForBuy(Long flightId, String seatNo) {
        FlightSeatDocument flightSeatDocument = new FlightSeatDocument();
        flightSeatDocument.setFlightId(flightId);
        flightSeatDocument.setSeatNo(seatNo);
        flightSeatDocument.setFlightSeatStatus(FlightSeatStatus.SOLD);
        flightSeatRepository.save(flightSeatDocument);
    }

    public List<FlightSeatDocument> getFlightSeats(Long flightId) {
        return flightSeatRepository.findAllByFlightId(flightId);
    }
}
