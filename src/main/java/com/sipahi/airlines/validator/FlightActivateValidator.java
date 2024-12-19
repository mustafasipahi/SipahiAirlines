package com.sipahi.airlines.validator;

import com.sipahi.airlines.advice.exception.FlightNotUpdatableException;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FlightActivateValidator {

    public void validate(FlightEntity flightEntity) {
        if (!flightEntity.getStatus().equals(FlightStatus.CREATED)) {
            throw new FlightNotUpdatableException();
        }
    }
}
