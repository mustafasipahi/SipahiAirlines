package com.sipahi.airlines.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DateUtilTest {

    @Test
    void shouldGetTimeWhenNullCase() {
        assertNull(DateUtil.getTime(null));
    }

    @Test
    void shouldGetTime() {
        LocalDateTime now = LocalDateTime.now();
        Long time = DateUtil.getTime(now);
        assertEquals(Timestamp.valueOf(now).getTime(), time);
    }

    @Test
    void shouldGetLocalDateTimeWhenNullCase() {
        assertNull(DateUtil.getLocalDateTime(null));
    }

    @Test
    void shouldGetLocalDateTime() {
        Long time = DateUtil.getTime(LocalDateTime.now());
        LocalDateTime localDateTime = DateUtil.getLocalDateTime(time);
        assertEquals(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()), localDateTime);
    }
}