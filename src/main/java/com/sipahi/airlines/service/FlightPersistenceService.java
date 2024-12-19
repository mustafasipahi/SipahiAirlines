package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.FlightNotFoundException;
import com.sipahi.airlines.converter.FlightConverter;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.dto.FlightDetailDto;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.model.response.FlightCreateResponse;
import com.sipahi.airlines.persistence.mysql.repository.FlightRepository;
import com.sipahi.airlines.persistence.mysql.specification.FlightSpecification;
import com.sipahi.airlines.validator.FlightActivateValidator;
import com.sipahi.airlines.validator.FlightCreateValidator;
import com.sipahi.airlines.validator.FlightDeleteValidator;
import com.sipahi.airlines.validator.FlightUpdateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sipahi.airlines.advice.constant.RedisConstant.FLIGHT_DETAIL;
import static com.sipahi.airlines.util.FlightNumberUtil.generateFlightNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightPersistenceService {

    private final FlightRepository flightRepository;
    private final AircraftService aircraftService;
    private final FlightCreateValidator createValidator;
    private final FlightUpdateValidator updateValidator;
    private final FlightDeleteValidator deleteValidator;
    private final FlightActivateValidator activateValidator;

    @Transactional
    public FlightCreateResponse create(FlightCreateRequest request) {
        createValidator.validate(request);
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber(generateFlightNumber());
        flightEntity.setName(request.getName());
        flightEntity.setDescription(request.getDescription());
        flightEntity.setFlightDate(request.getFlightDate());
        flightEntity.setAircraft(aircraftService.getDetail(request.getAircraftId()));
        flightEntity.setStatus(FlightStatus.CREATED);
        FlightEntity savedFlightEntity = flightRepository.save(flightEntity);
        log.info("Saved new flight: {}", savedFlightEntity.getFlightNumber());
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
        flightEntity.setName(Optional.ofNullable(request.getName())
                .orElse(flightEntity.getName()));
        flightEntity.setDescription(Optional.ofNullable(request.getDescription())
                .orElse(flightEntity.getDescription()));
        flightEntity.setAircraft(Optional.ofNullable(aircraftService.getDetail(request.getAircraftId()))
                .orElse(flightEntity.getAircraft()));
        flightEntity.setFlightDate(Optional.ofNullable(request.getFlightDate())
                .orElse(flightEntity.getFlightDate()));
        FlightEntity updatedFlightEntity = flightRepository.save(flightEntity);
        log.info("Updated flight: {}", updatedFlightEntity.getFlightNumber());
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

    @Cacheable(value = FLIGHT_DETAIL, key = "#flightNumber")
    public FlightDetailDto getDetail(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .filter(flightEntity -> flightEntity.getStatus().equals(FlightStatus.CREATED))
                .map(FlightConverter::toDetailDto)
                .orElseThrow(FlightNotFoundException::new);
    }

    public Page<FlightDto> getAll(FlightSearchRequest request) {
        Specification<FlightEntity> specification = FlightSpecification.filter(request);
        Sort sort = Sort.by(request.getDirection(), request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return flightRepository.findAll(specification, pageable)
                .map(FlightConverter::toDto);
    }
}
