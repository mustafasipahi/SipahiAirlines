package com.sipahi.airlines.configuration.redis;

import com.sipahi.airlines.configuration.properties.CacheTimeProperties;
import com.sipahi.airlines.configuration.properties.RedisProperties;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.sipahi.airlines.advice.constant.RedisConstant.*;

@Configuration
@EnableCaching
@AllArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;
    private final CacheTimeProperties cacheTimeProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(redisProperties.getHost());
        redisConf.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory())
                .withInitialCacheConfigurations(constructInitialCacheConfigurations())
                .transactionAware()
                .build();
    }

    private Map<String, RedisCacheConfiguration> constructInitialCacheConfigurations() {
        final Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

        final RedisCacheConfiguration flightCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheTimeProperties.getFlightDetailCacheMinute()))
                .disableCachingNullValues();

        final RedisCacheConfiguration aircraftDetailByIdCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheTimeProperties.getAircraftDetailCacheMinute()))
                .disableCachingNullValues();

        final RedisCacheConfiguration aircraftDetailByExternalIdCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheTimeProperties.getAircraftDetailCacheMinute()))
                .disableCachingNullValues();

        final RedisCacheConfiguration aircraftListCache = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheTimeProperties.getAircraftListCacheMinute()))
                .disableCachingNullValues();

        redisCacheConfigurationMap.put(FLIGHT_DETAIL, flightCache);
        redisCacheConfigurationMap.put(AIRCRAFT_DETAIL_BY_ID, aircraftDetailByIdCache);
        redisCacheConfigurationMap.put(AIRCRAFT_DETAIL_BY_EXTERNAL_ID, aircraftDetailByExternalIdCache);
        redisCacheConfigurationMap.put(AIRCRAFT_LIST, aircraftListCache);
        return redisCacheConfigurationMap;
    }
}