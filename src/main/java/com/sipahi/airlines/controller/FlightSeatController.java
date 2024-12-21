package com.sipahi.airlines.controller;

import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.model.request.FlightSeatUpdateRequest;
import com.sipahi.airlines.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flight/seat")
public class FlightSeatController {

    private final FlightService flightService;

    @GetMapping("/{flightNumber}")
    public List<FlightSeatDto> getSeatNumbers(@PathVariable String flightNumber) {
        return flightService.getAllFlightSeats(flightNumber);
    }

    @PutMapping("/{newStatus}")
    public void updateFlightSeat(@RequestBody FlightSeatUpdateRequest request, @PathVariable FlightSeatStatus newStatus) {
        flightService.updateFlightSeat(request, newStatus);
    }
}
