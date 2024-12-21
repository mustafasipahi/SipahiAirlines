package com.sipahi.airlines.controller;

import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.model.response.FlightCreateResponse;
import com.sipahi.airlines.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flight")
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    public FlightCreateResponse create(@Valid @RequestBody FlightCreateRequest request) {
        return flightService.create(request);
    }

    @PutMapping("/update")
    public void update(@Valid @RequestBody FlightUpdateRequest request) {
        flightService.update(request);
    }

    @DeleteMapping("/{flightNumber}")
    public void delete(@PathVariable String flightNumber) {
        flightService.delete(flightNumber);
    }

    @PutMapping("/activate/{flightNumber}")
    public void activate(@PathVariable String flightNumber) {
        flightService.activate(flightNumber);
    }

    @GetMapping("/{flightNumber}")
    public FlightDetailDto getDetail(@PathVariable String flightNumber) {
        return flightService.getDetail(flightNumber);
    }
}
