package com.sipahi.airlines.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static Long getTime(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(i -> Timestamp.valueOf(localDateTime).getTime())
                .orElse(null);
    }

    public static LocalDateTime getLocalDateTime(Long timestamp) {
        return Optional.ofNullable(timestamp)
                .map(i -> LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()))
                .orElse(null);
    }
}
