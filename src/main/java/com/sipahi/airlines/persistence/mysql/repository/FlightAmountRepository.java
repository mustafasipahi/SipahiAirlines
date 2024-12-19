package com.sipahi.airlines.persistence.mysql.repository;

import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightAmountRepository extends JpaRepository<FlightAmountEntity, Long> {

    Optional<FlightAmountEntity> findByFlightId(Long flightId);
}
