package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.AccountNotFoundException;
import com.sipahi.airlines.converter.AccountConverter;
import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.model.dto.AccountDto;
import com.sipahi.airlines.persistence.mongo.document.AccountDocument;
import com.sipahi.airlines.persistence.mongo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountDto getAccount(TestAccountType accountType) {
        return accountRepository.findByAccountType(accountType)
                .map(AccountConverter::toDto)
                .orElseThrow(AccountNotFoundException::new);
    }

    @Transactional
    public void updateAccount(String accountId, BigDecimal newAmount) {
        AccountDocument accountDocument = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
        accountDocument.setAmount(newAmount);
        accountRepository.save(accountDocument);
    }
}
