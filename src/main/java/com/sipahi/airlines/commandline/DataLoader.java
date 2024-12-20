package com.sipahi.airlines.commandline;

import com.sipahi.airlines.enums.AircraftStatus;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.mongo.document.AccountDocument;
import com.sipahi.airlines.persistence.mongo.repository.AccountRepository;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.mysql.repository.AircraftRepository;
import com.sipahi.airlines.persistence.mysql.repository.FlightAmountRepository;
import com.sipahi.airlines.persistence.mysql.repository.FlightRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.sipahi.airlines.util.FlightNumberUtil.generateFlightNumber;

@Slf4j
@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private static final Random RANDOM = new Random();

    private final RedisConnectionFactory redisConnectionFactory;
    private final MongoTemplate mongoTemplate;
    private final AircraftRepository aircraftRepository;
    private final AccountRepository accountRepository;
    private final FlightRepository flightRepository;
    private final FlightAmountRepository flightAmountRepository;

    @Override
    public void run(String... args) {
        flushRedis();
        flushMongo();
        List<AircraftEntity> aircraftList = saveAircraft();
        List<FlightEntity> flightEntities = saveFlight(aircraftList);
        saveFlightAmount(flightEntities);
        saveAccount();
    }

    private void flushRedis() {
        try {
            redisConnectionFactory.getConnection().flushAll();
            log.info("Redis cache has been cleared!");
        } catch (Exception e) {
            log.error("Failed to clear Redis cache", e);
        }
    }

    private void flushMongo() {
        try {
            mongoTemplate.getDb().drop();
            log.info("Mongo has been cleared!");
        } catch (Exception e) {
            log.error("Failed to clear Mongo database", e);
        }
    }

    private List<AircraftEntity> saveAircraft() {
        AircraftEntity aircraftEntity1 = new AircraftEntity();
        aircraftEntity1.setExternalId(UUID.randomUUID().toString());
        aircraftEntity1.setName("BOEING-747");
        aircraftEntity1.setEconomyRowCount(30);
        aircraftEntity1.setVipRowCount(3);
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
        aircraftEntity3.setVipRowCount(9);
        aircraftEntity3.setSeatsPerRow(6);
        aircraftEntity3.setStatus(AircraftStatus.NOT_AVAILABLE);
        aircraftRepository.deleteAll();
        return aircraftRepository.saveAll(Arrays.asList(aircraftEntity1, aircraftEntity2, aircraftEntity3));
    }

    private List<FlightEntity> saveFlight(List<AircraftEntity> aircraftList) {
        FlightEntity flightEntity1 = new FlightEntity();
        flightEntity1.setFlightNumber(generateFlightNumber());
        flightEntity1.setName("Istanbul-Bodrum");
        flightEntity1.setDescription("Direk Uçuş");
        flightEntity1.setFlightDate(LocalDateTime.of(2025, 1, 11, 13, 0));
        flightEntity1.setStatus(FlightStatus.AVAILABLE);
        flightEntity1.setAircraftId(randomAircraftId(aircraftList));

        FlightEntity flightEntity2 = new FlightEntity();
        flightEntity2.setFlightNumber(generateFlightNumber());
        flightEntity2.setName("Istanbul-Antalya");
        flightEntity2.setDescription("Aktarmalı Uçuş");
        flightEntity2.setFlightDate(LocalDateTime.of(2025, 1, 17, 19, 0));
        flightEntity2.setStatus(FlightStatus.AVAILABLE);
        flightEntity2.setAircraftId(randomAircraftId(aircraftList));

        FlightEntity flightEntity3 = new FlightEntity();
        flightEntity3.setFlightNumber(generateFlightNumber());
        flightEntity3.setName("Istanbul-Trabzon");
        flightEntity3.setDescription("Gecikmeli Uçuş");
        flightEntity3.setFlightDate(LocalDateTime.of(2025, 2, 3, 9, 0));
        flightEntity3.setStatus(FlightStatus.AVAILABLE);
        flightEntity3.setAircraftId(randomAircraftId(aircraftList));
        flightRepository.deleteAll();
        return flightRepository.saveAll(Arrays.asList(flightEntity1, flightEntity2, flightEntity3));
    }

    private void saveFlightAmount(List<FlightEntity> flightEntityList) {
        FlightAmountEntity amount1 = new FlightAmountEntity();
        amount1.setVip(new BigDecimal("1200"));
        amount1.setEconomy(new BigDecimal("1000"));
        amount1.setFlightId(randomFlightId(flightEntityList));

        FlightAmountEntity amount2 = new FlightAmountEntity();
        amount2.setVip(new BigDecimal("7400"));
        amount2.setEconomy(new BigDecimal("5800"));
        amount2.setFlightId(randomFlightId(flightEntityList));

        FlightAmountEntity amount3 = new FlightAmountEntity();
        amount3.setVip(new BigDecimal("12650"));
        amount3.setEconomy(new BigDecimal("11750"));
        amount3.setFlightId(randomFlightId(flightEntityList));
        flightAmountRepository.deleteAll();
        flightAmountRepository.saveAll(Arrays.asList(amount1, amount2, amount3));
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

    private Long randomAircraftId(List<AircraftEntity> aircraftList) {
        int index = RANDOM.nextInt(aircraftList.size());
        return aircraftList.remove(index).getId();
    }

    private Long randomFlightId(List<FlightEntity> flightEntityList) {
        int index = RANDOM.nextInt(flightEntityList.size());
        return flightEntityList.remove(index).getId();
    }
}
