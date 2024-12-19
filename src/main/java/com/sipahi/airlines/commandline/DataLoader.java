package com.sipahi.airlines.commandline;

import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.persistence.entity.AircraftEntity;
import com.sipahi.airlines.persistence.repository.AircraftRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AircraftRepository aircraftRepository;

    @Override
    public void run(String... args) {
        AircraftEntity aircraftEntity1 = new AircraftEntity();
        aircraftEntity1.setExternalId(UUID.randomUUID().toString());
        aircraftEntity1.setName("BOEING-747");
        aircraftEntity1.setPassengerCount(300);
        aircraftEntity1.setStatus(AircraftStatus.AVAILABLE);

        AircraftEntity aircraftEntity2 = new AircraftEntity();
        aircraftEntity2.setExternalId(UUID.randomUUID().toString());
        aircraftEntity2.setName("BOEING-457");
        aircraftEntity2.setPassengerCount(400);
        aircraftEntity2.setStatus(AircraftStatus.AVAILABLE);

        AircraftEntity aircraftEntity3 = new AircraftEntity();
        aircraftEntity3.setExternalId(UUID.randomUUID().toString());
        aircraftEntity3.setName("BOEING-341");
        aircraftEntity3.setPassengerCount(100);
        aircraftEntity3.setStatus(AircraftStatus.NOT_AVAILABLE);

        aircraftRepository.saveAll(Arrays.asList(aircraftEntity1, aircraftEntity2, aircraftEntity3));
    }
}
