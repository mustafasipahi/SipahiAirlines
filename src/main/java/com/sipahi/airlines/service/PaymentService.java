package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.exception.InsufficientAmountException;
import com.sipahi.airlines.advice.exception.SeatNotFoundException;
import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.model.dto.AccountDto;
import com.sipahi.airlines.persistence.model.dto.FlightSeatDto;
import com.sipahi.airlines.persistence.model.request.BuySeatRequest;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.sipahi.airlines.util.FlightSeatUtil.getAvailableSeats;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final FlightService flightService;
    private final FlightSeatService flightSeatService;
    private final AccountService accountService;

    @Transactional
    public void buySeat(BuySeatRequest request, TestAccountType accountType) {
        FlightEntity flight = flightService.getLockedDetail(request.getFlightNumber());
        List<FlightSeatDocument> flightSeats = flightSeatService.getFlightSeats(flight.getId());
        FlightSeatDto flightSeatDto = getFlightSeatDto(flight, flightSeats, request.getSeatNo());
        AccountDto account = accountService.getAccount(accountType);
        validateAmount(account.getAmount(), flightSeatDto.getAmount());
        flightSeatService.saveForBuy(flight.getId(), request.getSeatNo());
        accountService.updateAccount(account.getId(), account.getAmount().subtract(flightSeatDto.getAmount()));
        log.info("Bought seat: {} for flight: {}", request.getSeatNo(), flight.getId());
    }

    private FlightSeatDto getFlightSeatDto(FlightEntity flight, List<FlightSeatDocument> flightSeats, String seatNo) {
        List<FlightSeatDto> availableSeats = getAvailableSeats(flight, flightSeats);
        return availableSeats.stream()
                .filter(seatDocument -> seatDocument.getSeatNo().equals(seatNo))
                .filter(seatDocument -> seatDocument.getStatus().equals(FlightSeatStatus.AVAILABLE))
                .findFirst()
                .orElseThrow(SeatNotFoundException::new);
    }

    private void validateAmount(BigDecimal accountAmount, BigDecimal flightAmount) {
        if (accountAmount.compareTo(flightAmount) < 0) {
            throw new InsufficientAmountException();
        }
    }
}
