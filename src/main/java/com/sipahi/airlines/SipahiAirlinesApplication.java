package com.sipahi.airlines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EntityScan(basePackages = "com.sipahi.airlines.persistence.mysql.entity")
@EnableJpaRepositories(basePackages = "com.sipahi.airlines.persistence.mysql.repository")
@EnableMongoRepositories(basePackages = "com.sipahi.airlines.persistence.mongo.repository")
public class SipahiAirlinesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SipahiAirlinesApplication.class, args);
    }
}
