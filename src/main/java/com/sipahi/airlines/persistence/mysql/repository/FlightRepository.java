package com.sipahi.airlines.persistence.mysql.repository;

import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<FlightEntity, Long>,
        PagingAndSortingRepository<FlightEntity, Long>, JpaSpecificationExecutor<FlightEntity> {

    Optional<FlightEntity> findByNameAndFlightDate(String name, LocalDateTime flightDate);

    Optional<FlightEntity> findByFlightNumber(String flightNumber);
}
