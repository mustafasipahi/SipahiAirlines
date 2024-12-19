package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.AircraftNotFoundException;
import com.sipahi.airlines.converter.AircraftConverter;
import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.persistence.entity.AircraftEntity;
import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import com.sipahi.airlines.persistence.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sipahi.airlines.advice.constant.RedisConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    @Cacheable(value = AIRCRAFT_LIST)
    public List<AircraftDto> getAll() {
        return aircraftRepository.findAllByStatus(AircraftStatus.AVAILABLE).stream()
                .map(AircraftConverter::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = AIRCRAFT_DETAIL, key = "#externalId")
    public AircraftEntity getDetail(String externalId) {
        return aircraftRepository.findByExternalId(externalId)
                .orElseThrow(AircraftNotFoundException::new);
    }
}
