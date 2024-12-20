package com.sipahi.airlines.validator;

import com.sipahi.airlines.advice.exception.FlightNotUpdatableException;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FlightUpdateValidator {

    public void validate(FlightUpdateRequest request, FlightEntity flightEntity) {
        LocalDateTime now = LocalDateTime.now();
        if (!request.getFlightDate().isBefore(now)) {
            throw new FlightNotUpdatableException();
        }
        if (!flightEntity.getStatus().equals(FlightStatus.CREATED)) {
            throw new FlightNotUpdatableException();
        }
    }
}
