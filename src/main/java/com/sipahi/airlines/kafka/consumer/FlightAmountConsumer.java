package com.sipahi.airlines.kafka.consumer;

import com.sipahi.airlines.persistence.model.event.FlightAmountCreateEvent;
import com.sipahi.airlines.persistence.model.event.FlightAmountUpdateEvent;
import com.sipahi.airlines.service.FlightAmountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightAmountConsumer {

    private final FlightAmountService flightAmountService;

    @KafkaListener(topics = "${kafka.topic.saveFlightAmount}", containerFactory = "kafkaListenerContainerFactory")
    private void sendFlightAmountCreateEvent(FlightAmountCreateEvent event) {
        log.info("Consumed flight amount create event: {}", event.toString());
        flightAmountService.save(event);
    }

    @KafkaListener(topics = "${kafka.topic.updateFlightAmount}", containerFactory = "kafkaListenerContainerFactory")
    private void sendFlightAmountUpdateEvent(FlightAmountUpdateEvent event) {
        log.info("Consumed flight amount update event: {}", event.toString());
        flightAmountService.update(event);
    }
}
