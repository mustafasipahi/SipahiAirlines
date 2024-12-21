package com.sipahi.airlines.validator;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import com.sipahi.airlines.advice.exception.FlightAlreadyExistException;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.mysql.repository.FlightRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightCreateValidatorTest {

    @InjectMocks
    private FlightCreateValidator validator;
    @Mock
    private FlightRepository flightRepository;

    @Test
    void shouldThrowFlightAlreadyExistException() {
        FlightCreateRequest request = FlightCreateRequest.builder().build();
        request.setName(RandomStringUtils.randomAlphabetic(3));
        request.setFlightDate(LocalDateTime.now());

        when(flightRepository.findByNameAndFlightDate(request.getName(), request.getFlightDate()))
                .thenReturn(Optional.of(new FlightEntity()));

        FlightAlreadyExistException exception = assertThrows(
                FlightAlreadyExistException.class,
                () -> validator.validate(request));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(ErrorCodes.FLIGHT_ALREADY_EXIST, exception.getCode());
        assertEquals("Flight Already Exist!", exception.getMessage());
    }
}