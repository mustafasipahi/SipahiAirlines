package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.FlightAlreadyExistException;
import com.sipahi.airlines.advice.exception.FlightNotFoundException;
import com.sipahi.airlines.converter.FlightConverter;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.model.response.FlightCreateResponse;
import com.sipahi.airlines.persistence.repository.FlightRepository;
import com.sipahi.airlines.persistence.specification.FlightSpecification;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static com.sipahi.airlines.advice.constant.RedisConstant.FLIGHT_DETAIL;
import static com.sipahi.airlines.util.FlightNumberUtil.generateFlightNumber;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightPersistenceService {

    private final FlightRepository flightRepository;
    private final AircraftService aircraftService;

    @Transactional
    public FlightCreateResponse create(FlightCreateRequest request) {
        validateAlreadyExist(request.getName(), request.getFlightDate());
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightNumber(generateFlightNumber());
        flightEntity.setName(request.getName());
        flightEntity.setFlightDate(request.getFlightDate());
        flightEntity.setAircraftEntity(aircraftService.getDetail(request.getAircraftId()));
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
        validateAlreadyExist(request.getName(), request.getFlightDate());
        final FlightEntity flightEntity = flightRepository.findByFlightNumber(request.getFlightNumber())
                .orElseThrow(FlightNotFoundException::new);
        flightEntity.setName(Optional.ofNullable(request.getName())
                .orElse(flightEntity.getName()));
        flightEntity.setAircraftEntity(Optional.ofNullable(aircraftService.getDetail(request.getAircraftId()))
                .orElse(flightEntity.getAircraftEntity()));
        flightEntity.setFlightDate(Optional.ofNullable(request.getFlightDate())
                .orElse(flightEntity.getFlightDate()));
        FlightEntity updatedFlightEntity = flightRepository.save(flightEntity);
        log.info("Updated flight: {}", updatedFlightEntity.getFlightNumber());
    }

    @Cacheable(value = FLIGHT_DETAIL, key = "#flightNumber")
    public FlightDto getDetail(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .map(FlightConverter::toDto)
                .orElseThrow(FlightNotFoundException::new);
    }

    public Page<FlightDto> getAll(FlightSearchRequest request) {
        Specification<FlightEntity> specification = FlightSpecification.filter(request);
        Sort sort = Sort.by(request.getDirection(), request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return flightRepository.findAll(specification, pageable)
                .map(FlightConverter::toDto);
    }

    private void validateAlreadyExist(String name, LocalDateTime flightDate) {
        Optional<FlightEntity> alreadyExist = flightRepository.findByNameAndFlightDate(name, flightDate);
        if (alreadyExist.isPresent()) {
            throw new FlightAlreadyExistException();
        }
    }
}
