package com.sipahi.airlines.converter;

import com.sipahi.airlines.persistence.model.dto.AccountDto;
import com.sipahi.airlines.persistence.mongo.document.AccountDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountConverter {

    public static AccountDto toDto(AccountDocument document) {
        return AccountDto.builder()
                .id(document.getId())
                .amount(document.getAmount())
                .build();
    }
}
