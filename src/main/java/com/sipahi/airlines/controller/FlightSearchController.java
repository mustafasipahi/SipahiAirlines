package com.sipahi.airlines.controller;

import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import com.sipahi.airlines.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flight/search")
public class FlightSearchController {

    private final ElasticSearchService elasticSearchService;

    @GetMapping
    public List<FlightDto> search(@ModelAttribute FlightSearchRequest request) {
        return elasticSearchService.search(request);
    }
}
