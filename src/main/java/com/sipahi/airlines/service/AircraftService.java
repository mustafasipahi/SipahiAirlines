package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.AircraftNotFoundException;
import com.sipahi.airlines.converter.AircraftConverter;
import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import com.sipahi.airlines.persistence.model.request.AircraftCreateRequest;
import com.sipahi.airlines.persistence.model.request.UpdateUpdateRequest;
import com.sipahi.airlines.persistence.model.response.AircraftCreateResponse;
import com.sipahi.airlines.persistence.mysql.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sipahi.airlines.advice.constant.RedisConstant.AIRCRAFT_DETAIL;
import static com.sipahi.airlines.advice.constant.RedisConstant.AIRCRAFT_LIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    public AircraftCreateResponse create(AircraftCreateRequest request) {
        AircraftEntity aircraftEntity = new AircraftEntity();
        aircraftEntity.setExternalId(UUID.randomUUID().toString());
        aircraftEntity.setName(request.getName());
        aircraftEntity.setPassengerCount(request.getPassengerCount());
        AircraftEntity savedAircraft = aircraftRepository.save(aircraftEntity);
        log.info("Saved aircraft id: {} external id: {}", savedAircraft.getId(), savedAircraft.getExternalId());
        return AircraftCreateResponse.builder()
                .externalId(savedAircraft.getExternalId())
                .build();
    }

    @CacheEvict(value = AIRCRAFT_DETAIL, key = "#request.externalId")
    public void update(UpdateUpdateRequest request) {
        AircraftEntity aircraftEntity = aircraftRepository.findByExternalId(request.getExternalId())
                .orElseThrow(AircraftNotFoundException::new);
        aircraftEntity.setName(Optional.ofNullable(request.getName())
                .orElse(aircraftEntity.getName()));
        aircraftEntity.setPassengerCount(Optional.ofNullable(request.getPassengerCount())
                .orElse(aircraftEntity.getPassengerCount()));
        aircraftEntity.setStatus(Optional.ofNullable(request.getStatus())
                .orElse(aircraftEntity.getStatus()));
        AircraftEntity updatedAircraft = aircraftRepository.save(aircraftEntity);
        log.info("Updated aircraft id: {} external id: {}", updatedAircraft.getId(), updatedAircraft.getExternalId());
    }

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
