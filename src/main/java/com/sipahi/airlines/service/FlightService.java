package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.FlightNotFoundException;
import com.sipahi.airlines.converter.FlightConverter;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.model.response.FlightCreateResponse;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.mysql.repository.FlightRepository;
import com.sipahi.airlines.validator.FlightActivateValidator;
import com.sipahi.airlines.validator.FlightCreateValidator;
import com.sipahi.airlines.validator.FlightDeleteValidator;
import com.sipahi.airlines.validator.FlightUpdateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sipahi.airlines.advice.constant.RedisConstant.FLIGHT_DETAIL;
import static com.sipahi.airlines.util.FlightNumberUtil.generateFlightNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final AircraftService aircraftService;
    private final FlightConverter flightConverter;
    private final FlightAmountService flightAmountService;
    private final ElasticSearchService elasticSearchService;

    private final FlightCreateValidator createValidator;
    private final FlightUpdateValidator updateValidator;
    private final FlightDeleteValidator deleteValidator;
    private final FlightActivateValidator activateValidator;

    @Transactional
    public FlightCreateResponse create(FlightCreateRequest request) {
        createValidator.validate(request);
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setAircraftId(getAircraftId(request.getAircraftId()));
        flightEntity.setFlightNumber(generateFlightNumber());
        flightEntity.setName(request.getName());
        flightEntity.setDescription(request.getDescription());
        flightEntity.setFlightDate(request.getFlightDate());
        flightEntity.setStatus(FlightStatus.CREATED);
        FlightEntity savedFlightEntity = flightRepository.save(flightEntity);
        log.info("Saved new flight: {}", savedFlightEntity.getFlightNumber());
        flightAmountService.createFlightAmount(savedFlightEntity.getId(), request);
        elasticSearchService.saveFlightEvent(flightEntity);
        return FlightCreateResponse.builder()
                .flightNumber(savedFlightEntity.getFlightNumber())
                .build();
    }

    @Transactional
    @CacheEvict(value = FLIGHT_DETAIL, key = "#request.flightNumber")
    public void update(FlightUpdateRequest request) {
        final FlightEntity flightEntity = flightRepository.findByFlightNumber(request.getFlightNumber())
                .orElseThrow(FlightNotFoundException::new);
        updateValidator.validate(request, flightEntity);
        flightEntity.setAircraftId(Optional.ofNullable(request.getAircraftId())
                .map(this::getAircraftId)
                .orElse(flightEntity.getAircraftId()));
        flightEntity.setName(Optional.ofNullable(request.getName())
                .orElse(flightEntity.getName()));
        flightEntity.setDescription(Optional.ofNullable(request.getDescription())
                .orElse(flightEntity.getDescription()));
        flightEntity.setFlightDate(Optional.ofNullable(request.getFlightDate())
                .orElse(flightEntity.getFlightDate()));
        FlightEntity updatedFlightEntity = flightRepository.save(flightEntity);
        log.info("Updated flight: {}", updatedFlightEntity.getFlightNumber());
        flightAmountService.updateFlightAmount(updatedFlightEntity.getId(), request);
        elasticSearchService.saveFlightEvent(flightEntity);
    }

    @Transactional
    @CacheEvict(value = FLIGHT_DETAIL, key = "#flightNumber")
    public void delete(String flightNumber) {
        final FlightEntity flightEntity = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(FlightNotFoundException::new);
        deleteValidator.validate(flightEntity);
        flightEntity.setStatus(FlightStatus.DELETED);
        FlightEntity updatedFlightEntity = flightRepository.save(flightEntity);
        log.info("Deleted flight: {}", updatedFlightEntity.getFlightNumber());
    }

    @Transactional
    public void activate(String flightNumber) {
        FlightEntity flightEntity = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(FlightNotFoundException::new);
        activateValidator.validate(flightEntity);
        flightEntity.setStatus(FlightStatus.AVAILABLE);
        FlightEntity activatedFlightEntity = flightRepository.save(flightEntity);
        log.info("Activated flight: {}", activatedFlightEntity.getFlightNumber());
    }

    @Transactional
    @Cacheable(value = FLIGHT_DETAIL, key = "#flightNumber")
    public FlightDetailDto getDetail(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .filter(flightEntity -> flightEntity.getStatus().equals(FlightStatus.AVAILABLE))
                .map(flightConverter::toDetailDto)
                .orElseThrow(FlightNotFoundException::new);
    }

    @Transactional
    public FlightEntity getLockedDetail(String flightNumber) {
        return flightRepository.findByFlightNumberForWrite(flightNumber, FlightStatus.AVAILABLE)
                .orElseThrow(FlightNotFoundException::new);
    }

    private Long getAircraftId(String externalId) {
        return aircraftService.getDetailByExternalId(externalId).getId();
    }
}
