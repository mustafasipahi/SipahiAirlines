package com.sipahi.airlines.validator;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import com.sipahi.airlines.advice.exception.FlightNotUpdatableException;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlightActivateValidatorTest {

    @InjectMocks
    private FlightActivateValidator validator;

    @Test
    void shouldThrowFlightNotUpdatableException() {
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setStatus(FlightStatus.DELETED);
        FlightNotUpdatableException exception = assertThrows(
                FlightNotUpdatableException.class,
                () -> validator.validate(flightEntity));
        assertEquals(ErrorCodes.FLIGHT_NOT_UPDATABLE, exception.getCode());
        assertEquals("Flight Not Updatable!", exception.getMessage());
    }
}