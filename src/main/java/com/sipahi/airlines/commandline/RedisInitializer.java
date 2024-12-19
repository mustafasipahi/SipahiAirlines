package com.sipahi.airlines.commandline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Slf4j
@Component
public class RedisInitializer implements CommandLineRunner {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisInitializer(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void run(String... args) {
        redisConnectionFactory.getConnection().flushAll();
        log.info("Redis cache has been cleared!");
    }
}
