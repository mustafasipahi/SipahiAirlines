package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.FlightAmountNotFoundException;
import com.sipahi.airlines.kafka.producer.FlightAmountProducer;
import com.sipahi.airlines.persistence.model.event.FlightAmountCreateEvent;
import com.sipahi.airlines.persistence.model.event.FlightAmountUpdateEvent;
import com.sipahi.airlines.persistence.model.request.FlightCreateRequest;
import com.sipahi.airlines.persistence.model.request.FlightUpdateRequest;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.repository.FlightAmountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FlightAmountService {

    private final FlightAmountRepository flightAmountRepository;
    private final FlightAmountProducer flightAmountProducer;

    @Transactional
    public void save(FlightAmountCreateEvent event) {
        FlightAmountEntity flightAmountEntity = new FlightAmountEntity();
        flightAmountEntity.setEconomy(event.getEconomyAmount());
        flightAmountEntity.setVip(event.getVipAmount());
        flightAmountEntity.setFlightId(event.getFlightId());
        flightAmountRepository.save(flightAmountEntity);
    }

    @Transactional
    public void update(FlightAmountUpdateEvent event) {
        flightAmountRepository.findByFlightId(event.getFlightId())
                .ifPresent(flightAmountEntity -> {
                    flightAmountEntity.setEconomy(event.getEconomyAmount());
                    flightAmountEntity.setVip(event.getVipAmount());
                    flightAmountRepository.save(flightAmountEntity);
                });
    }

    @Transactional
    public void createFlightAmount(Long flightId, FlightCreateRequest request) {
        FlightAmountCreateEvent event = FlightAmountCreateEvent.builder()
                .vipAmount(request.getVipAmount())
                .economyAmount(request.getEconomyAmount())
                .flightId(flightId)
                .build();
        flightAmountProducer.sendFlightAmountCreateEvent(event);
    }

    @Transactional
    public void updateFlightAmount(Long updatedFlightId, FlightUpdateRequest request) {
        FlightAmountUpdateEvent event = FlightAmountUpdateEvent.builder()
                .vipAmount(request.getVipAmount())
                .economyAmount(request.getEconomyAmount())
                .flightId(updatedFlightId)
                .build();
        flightAmountProducer.sendFlightAmountUpdateEvent(event);
    }

    public FlightAmountEntity findByFlightId(Long flightId) {
        return flightAmountRepository.findByFlightId(flightId)
                .orElseThrow(FlightAmountNotFoundException::new);
    }
}
