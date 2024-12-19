package com.sipahi.airlines.validator;

import com.sipahi.airlines.advice.exception.FlightAlreadyExistException;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.mysql.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FlightCreateValidator {

    private final FlightRepository flightRepository;

    public void validate(FlightCreateRequest request) {
        Optional<FlightEntity> alreadyExist = flightRepository.findByNameAndFlightDate(request.getName(), request.getFlightDate());
        if (alreadyExist.isPresent()) {
            throw new FlightAlreadyExistException();
        }
    }
}
