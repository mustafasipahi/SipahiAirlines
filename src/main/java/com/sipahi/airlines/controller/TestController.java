package com.sipahi.airlines.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping
    @Operation(summary = "For Test")
    public String test() {
        String curl1 = "curl -X POST \"http://localhost:8080/payment/ACCOUNT_1\" -H \"Content-Type: application/json\" -d '{\"flightNumber\": _any_, \"seatNo\": _any_}'";
        String curl2 = "curl -X POST \"http://localhost:8080/payment/ACCOUNT_2\" -H \"Content-Type: application/json\" -d '{\"flightNumber\": _any_, \"seatNo\": _any_}'";
        return curl1 + "&" + curl2;
    }
}
