package com.sipahi.airlines.persistence.mongo.repository;

import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightSeatRepository extends MongoRepository<FlightSeatDocument, String> {

    List<FlightSeatDocument> findAllByFlightId(Long flightId);
}
