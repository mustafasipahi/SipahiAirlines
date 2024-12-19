package com.sipahi.airlines.persistence.mysql.repository;

import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {

    List<AircraftEntity> findAllByStatus(AircraftStatus status);

    Optional<AircraftEntity> findByExternalId(String externalId);
}
