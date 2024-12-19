package com.sipahi.airlines.controller;

import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.model.response.FlightCreateResponse;
import com.sipahi.airlines.service.FlightPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flight")
public class FlightController {

    private final FlightPersistenceService flightPersistenceService;

    @PostMapping("/create")
    public FlightCreateResponse create(@Valid @RequestBody FlightCreateRequest request) {
        return flightPersistenceService.create(request);
    }

    @PutMapping("/update")
    public void update(@Valid @RequestBody FlightUpdateRequest request) {
        flightPersistenceService.update(request);
    }

    @DeleteMapping("/{flightNumber}")
    public void delete(@PathVariable String flightNumber) {
        flightPersistenceService.delete(flightNumber);
    }

    @PutMapping("/{flightNumber}")
    public void activate(@PathVariable String flightNumber) {
        flightPersistenceService.activate(flightNumber);
    }

    @GetMapping("/{flightNumber}")
    public FlightDetailDto getDetail(@PathVariable String flightNumber) {
        return flightPersistenceService.getDetail(flightNumber);
    }

    @GetMapping("/search")
    public Page<FlightDto> getAll(@ModelAttribute FlightSearchRequest request) {
        return flightPersistenceService.getAll(request);
    }
}
