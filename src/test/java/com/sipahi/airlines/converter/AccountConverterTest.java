package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.model.dto.AccountDto;
import com.sipahi.airlines.persistence.mongo.document.AccountDocument;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccountConverterTest {

    @Test
    void shouldConvertToDto() {
        AccountDocument accountDocument = AccountDocument.builder()
                .id(RandomStringUtils.randomNumeric(3))
                .amount(new BigDecimal(RandomUtils.nextInt()))
                .build();
        AccountDto response = AccountConverter.toDto(accountDocument);

        assertEquals(accountDocument.getId(), response.getId());
        assertEquals(accountDocument.getAmount(), response.getAmount());
    }
}