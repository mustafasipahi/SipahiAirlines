package com.sipahi.airlines.persistence.mysql.repository;

import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<FlightEntity, Long>,
        PagingAndSortingRepository<FlightEntity, Long>, JpaSpecificationExecutor<FlightEntity> {

    Optional<FlightEntity> findByNameAndFlightDate(String name, LocalDateTime flightDate);

    Optional<FlightEntity> findByFlightNumber(String flightNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fe FROM FlightEntity fe WHERE fe.flightNumber = :flightNumber AND fe.status = :status")
    Optional<FlightEntity> findByFlightNumberForWrite(@Param("flightNumber") String flightNumber,
                                                      @Param("status") FlightStatus status);
}
