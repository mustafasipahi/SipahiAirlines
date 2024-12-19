package com.sipahi.airlines.controller;

import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import com.sipahi.airlines.persistence.model.request.AircraftCreateRequest;
import com.sipahi.airlines.persistence.model.request.AircraftUpdateRequest;
import com.sipahi.airlines.persistence.model.response.AircraftCreateResponse;
import com.sipahi.airlines.service.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping("/create")
    public AircraftCreateResponse create(@Valid @RequestBody AircraftCreateRequest request) {
        return aircraftService.create(request);
    }

    @PutMapping("/update")
    public void update(@Valid @RequestBody AircraftUpdateRequest request) {
        aircraftService.update(request);
    }

    @GetMapping
    public List<AircraftDto> getAll() {
        return aircraftService.getAll();
    }
}
