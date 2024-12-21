package com.sipahi.airlines.controller;

import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.model.request.BuySeatRequest;
import com.sipahi.airlines.persistence.model.response.PaymentResponse;
import com.sipahi.airlines.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{accountType}")
    public PaymentResponse buySeat(@RequestBody BuySeatRequest request, @PathVariable TestAccountType accountType) {
        return paymentService.buySeat(request, accountType);
    }
}
