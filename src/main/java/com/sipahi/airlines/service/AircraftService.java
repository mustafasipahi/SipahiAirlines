package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.AircraftNotFoundException;
import com.sipahi.airlines.converter.AircraftConverter;
import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.model.dto.AircraftDto;
import com.sipahi.airlines.persistence.model.request.AircraftCreateRequest;
import com.sipahi.airlines.persistence.model.request.AircraftUpdateRequest;
import com.sipahi.airlines.persistence.model.response.AircraftCreateResponse;
import com.sipahi.airlines.persistence.mysql.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sipahi.airlines.advice.constant.RedisConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    @Transactional
    public AircraftCreateResponse create(AircraftCreateRequest request) {
        AircraftEntity aircraftEntity = new AircraftEntity();
        aircraftEntity.setExternalId(UUID.randomUUID().toString());
        aircraftEntity.setName(request.getName());
        aircraftEntity.setEconomyRowCount(request.getEconomyRowCount());
        aircraftEntity.setVipRowCount(request.getVipRowCount());
        aircraftEntity.setSeatsPerRow(request.getSeatsPerRow());
        AircraftEntity savedAircraft = aircraftRepository.save(aircraftEntity);
        log.info("Saved aircraft id: {} external id: {}", savedAircraft.getId(), savedAircraft.getExternalId());
        return AircraftCreateResponse.builder()
                .externalId(savedAircraft.getExternalId())
                .build();
    }

    @CacheEvict(value = AIRCRAFT_DETAIL_BY_EXTERNAL_ID, key = "#request.externalId")
    public void update(AircraftUpdateRequest request) {
        AircraftEntity aircraftEntity = aircraftRepository.findByExternalId(request.getExternalId())
                .orElseThrow(AircraftNotFoundException::new);
        aircraftEntity.setName(Optional.ofNullable(request.getName())
                .orElse(aircraftEntity.getName()));
        aircraftEntity.setEconomyRowCount(Optional.ofNullable(request.getEconomyRowCount())
                .orElse(aircraftEntity.getEconomyRowCount()));
        aircraftEntity.setVipRowCount(Optional.ofNullable(request.getVipRowCount())
                .orElse(aircraftEntity.getVipRowCount()));
        aircraftEntity.setSeatsPerRow(Optional.ofNullable(request.getSeatsPerRow())
                .orElse(aircraftEntity.getSeatsPerRow()));
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

    @Cacheable(value = AIRCRAFT_DETAIL_BY_EXTERNAL_ID, key = "#externalId")
    public AircraftEntity getDetailByExternalId(String externalId) {
        return aircraftRepository.findByExternalId(externalId)
                .orElseThrow(AircraftNotFoundException::new);
    }

    @Cacheable(value = AIRCRAFT_DETAIL_BY_ID, key = "#id")
    public AircraftEntity getDetailById(Long id) {
        return aircraftRepository.findById(id)
                .orElseThrow(AircraftNotFoundException::new);
    }
}
