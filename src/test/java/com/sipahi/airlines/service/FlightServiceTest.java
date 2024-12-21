package com.sipahi.airlines.service;

import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.model.response.FlightCreateResponse;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.mysql.repository.FlightRepository;
import com.sipahi.airlines.validator.FlightActivateValidator;
import com.sipahi.airlines.validator.FlightCreateValidator;
import com.sipahi.airlines.validator.FlightDeleteValidator;
import com.sipahi.airlines.validator.FlightUpdateValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AircraftService aircraftService;
    @Mock
    private FlightAmountService flightAmountService;
    @Mock
    private ElasticSearchService elasticSearchService;
    @Mock
    private FlightCreateValidator createValidator;
    @Mock
    private FlightUpdateValidator updateValidator;
    @Mock
    private FlightDeleteValidator deleteValidator;
    @Mock
    private FlightActivateValidator activateValidator;

    @Captor
    private ArgumentCaptor<FlightEntity> flightCaptor;

    @Test
    void shouldCreate() {
        FlightCreateRequest request = new FlightCreateRequest();
        request.setAircraftId(RandomStringUtils.randomNumeric(3));
        request.setName(RandomStringUtils.randomAlphabetic(3));
        request.setDescription(RandomStringUtils.randomAlphabetic(3));
        request.setFlightDate(LocalDateTime.now());

        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setId(RandomUtils.nextLong());

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(RandomUtils.nextLong());
        flightEntity.setFlightNumber(RandomStringUtils.randomNumeric(3));

        when(aircraftService.getDetailByExternalId(request.getAircraftId())).thenReturn(aircraft);
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        FlightCreateResponse response = flightService.create(request);
        assertEquals(flightEntity.getFlightNumber(), response.getFlightNumber());

        verify(flightRepository).save(flightCaptor.capture());
        FlightEntity flightCaptorValue = flightCaptor.getValue();
        assertEquals(aircraft.getId(), flightCaptorValue.getAircraftId());
        assertNotNull(flightCaptorValue.getFlightNumber());
        assertEquals(request.getName(), flightCaptorValue.getName());
        assertEquals(request.getDescription(), flightCaptorValue.getDescription());
        assertEquals(request.getFlightDate(), flightCaptorValue.getFlightDate());
        assertEquals(FlightStatus.CREATED, flightCaptorValue.getStatus());

        verify(createValidator).validate(request);
        verify(flightAmountService).createFlightAmount(flightEntity.getId(), request);
        verify(elasticSearchService).saveFlightEvent(flightEntity);
    }

    @Test
    void shouldUpdate() {
        FlightUpdateRequest request = new FlightUpdateRequest();
        request.setFlightNumber(RandomStringUtils.randomNumeric(3));
        request.setName(RandomStringUtils.randomAlphabetic(3));
        request.setDescription(RandomStringUtils.randomAlphabetic(3));
        request.setFlightDate(LocalDateTime.now());
        request.setAircraftId(RandomStringUtils.randomNumeric(3));

        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setId(RandomUtils.nextLong());

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(RandomUtils.nextLong());
        flightEntity.setFlightNumber(request.getFlightNumber());

        when(aircraftService.getDetailByExternalId(request.getAircraftId())).thenReturn(aircraft);
        when(flightRepository.findByFlightNumber(request.getFlightNumber())).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        flightService.update(request);

        verify(flightRepository).save(flightCaptor.capture());
        FlightEntity flightCaptorValue = flightCaptor.getValue();
        assertEquals(aircraft.getId(), flightCaptorValue.getAircraftId());
        assertEquals(request.getName(), flightCaptorValue.getName());
        assertEquals(request.getDescription(), flightCaptorValue.getDescription());
        assertEquals(request.getFlightDate(), flightCaptorValue.getFlightDate());

        verify(updateValidator).validate(request, flightEntity);
        verify(flightAmountService).updateFlightAmount(flightEntity.getId(), request);
        verify(elasticSearchService).saveFlightEvent(flightEntity);
    }

    @Test
    void shouldDelete() {
        String flightNumber = RandomStringUtils.randomNumeric(3);

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(RandomUtils.nextLong());
        flightEntity.setFlightNumber(flightNumber);

        when(flightRepository.findByFlightNumber(flightNumber)).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        flightService.delete(flightNumber);

        verify(flightRepository).save(flightCaptor.capture());
        FlightEntity flightCaptorValue = flightCaptor.getValue();
        assertEquals(FlightStatus.DELETED, flightCaptorValue.getStatus());

        verify(deleteValidator).validate(flightEntity);
        verify(elasticSearchService).saveFlightEvent(flightEntity);
    }

    @Test
    void shouldActivate() {
        String flightNumber = RandomStringUtils.randomNumeric(3);

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(RandomUtils.nextLong());
        flightEntity.setFlightNumber(flightNumber);

        when(flightRepository.findByFlightNumber(flightNumber)).thenReturn(Optional.of(flightEntity));
        when(flightRepository.save(any(FlightEntity.class))).thenReturn(flightEntity);

        flightService.activate(flightNumber);

        verify(flightRepository).save(flightCaptor.capture());
        FlightEntity flightCaptorValue = flightCaptor.getValue();
        assertEquals(FlightStatus.AVAILABLE, flightCaptorValue.getStatus());

        verify(activateValidator).validate(flightEntity);
        verify(elasticSearchService).saveFlightEvent(flightEntity);
    }
}