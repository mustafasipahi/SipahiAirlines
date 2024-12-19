package com.sipahi.airlines.commandline;

import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.mongo.document.AccountDocument;
import com.sipahi.airlines.persistence.mongo.repository.AccountRepository;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.repository.AircraftRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AircraftRepository aircraftRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        saveAircraft();
        saveAccount();
    }

    private void saveAircraft() {
        AircraftEntity aircraftEntity1 = new AircraftEntity();
        aircraftEntity1.setExternalId(UUID.randomUUID().toString());
        aircraftEntity1.setName("BOEING-747");
        aircraftEntity1.setEconomyRowCount(30);
        aircraftEntity1.setVipRowCount(5);
        aircraftEntity1.setSeatsPerRow(6);
        aircraftEntity1.setStatus(AircraftStatus.AVAILABLE);

        AircraftEntity aircraftEntity2 = new AircraftEntity();
        aircraftEntity2.setExternalId(UUID.randomUUID().toString());
        aircraftEntity2.setName("BOEING-457");
        aircraftEntity2.setEconomyRowCount(25);
        aircraftEntity2.setVipRowCount(5);
        aircraftEntity2.setSeatsPerRow(6);
        aircraftEntity2.setStatus(AircraftStatus.AVAILABLE);

        AircraftEntity aircraftEntity3 = new AircraftEntity();
        aircraftEntity3.setExternalId(UUID.randomUUID().toString());
        aircraftEntity3.setName("BOEING-341");
        aircraftEntity3.setEconomyRowCount(30);
        aircraftEntity3.setVipRowCount(5);
        aircraftEntity3.setSeatsPerRow(6);
        aircraftEntity3.setStatus(AircraftStatus.NOT_AVAILABLE);

        aircraftRepository.saveAll(Arrays.asList(aircraftEntity1, aircraftEntity2, aircraftEntity3));
    }

    private void saveAccount() {
        AccountDocument accountDocument1 = new AccountDocument();
        accountDocument1.setAmount(new BigDecimal("20000"));
        accountDocument1.setAccountType(TestAccountType.ACCOUNT_1);

        AccountDocument accountDocument2 = new AccountDocument();
        accountDocument2.setAmount(new BigDecimal("30000"));
        accountDocument2.setAccountType(TestAccountType.ACCOUNT_2);

        accountRepository.saveAll(Arrays.asList(accountDocument1, accountDocument2));
    }
}
