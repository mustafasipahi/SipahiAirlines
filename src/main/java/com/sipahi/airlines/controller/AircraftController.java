package com.sipahi.airlines.controller;

import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import com.sipahi.airlines.service.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;

    @GetMapping
    public List<AircraftDto> getAll() {
        return aircraftService.getAll();
    }
}
