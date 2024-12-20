package com.sipahi.airlines.controller;

import com.sipahi.airlines.persistence.model.dto.AccountDto;
import com.sipahi.airlines.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll();
    }
}
