package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.SeatNotAvailableException;
import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.model.request.FlightSeatUpdateRequest;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mongo.repository.FlightSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sipahi.airlines.util.TicketUtil.generateTicketNo;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSeatService {

    private final FlightSeatRepository flightSeatRepository;

    @Transactional
    public String saveForBuy(Long flightId, String flightNumber, String seatNo) {
        FlightSeatDocument flightSeatDocument = new FlightSeatDocument();
        flightSeatDocument.setFlightId(flightId);
        flightSeatDocument.setSeatNo(seatNo);
        flightSeatDocument.setFlightSeatStatus(FlightSeatStatus.SOLD);
        flightSeatDocument.setTicketNo(generateTicketNo(flightNumber, seatNo));
        flightSeatRepository.save(flightSeatDocument);
        return flightSeatDocument.getTicketNo();
    }

    public void updateFlightSeat(FlightSeatUpdateRequest request,
                                 List<FlightSeatDto> allFlightSeats,
                                 Long flightId,
                                 FlightSeatStatus newStatus) {
        FlightSeatDto flightSeatDto = allFlightSeats.stream()
                .filter(flightSeat -> flightSeat.getSeatNo().equalsIgnoreCase(request.getSeatNo()))
                .filter(flightSeat -> flightSeat.getStatus().equals(FlightSeatStatus.AVAILABLE))
                .findAny()
                .orElseThrow(SeatNotAvailableException::new);
        FlightSeatDocument flightSeatDocument = new FlightSeatDocument();
        flightSeatDocument.setFlightId(flightId);
        flightSeatDocument.setSeatNo(flightSeatDto.getSeatNo());
        flightSeatDocument.setFlightSeatStatus(newStatus);
        flightSeatRepository.save(flightSeatDocument);
    }

    public List<FlightSeatDocument> getFlightSeats(Long flightId) {
        return flightSeatRepository.findAllByFlightId(flightId);
    }
}
