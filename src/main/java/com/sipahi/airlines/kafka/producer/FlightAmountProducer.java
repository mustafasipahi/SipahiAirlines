package com.sipahi.airlines.kafka.producer;

import com.sipahi.airlines.configuration.properties.KafkaProperties;
import com.sipahi.airlines.persistence.model.event.FlightAmountCreateEvent;
import com.sipahi.airlines.persistence.model.event.FlightAmountUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightAmountProducer {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Async
    public void sendFlightAmountCreateEvent(FlightAmountCreateEvent event) {
        kafkaTemplate.send(kafkaProperties.getTopic().getSaveFlightAmount(), event);
        log.info("Sent flight amount create event: {}", event);
    }

    @Async
    public void sendFlightAmountUpdateEvent(FlightAmountUpdateEvent event) {
        kafkaTemplate.send(kafkaProperties.getTopic().getUpdateFlightAmount(), event);
        log.info("Sent flight amount update event: {}", event);
    }
}
