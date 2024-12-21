package com.sipahi.airlines.service;

import com.sipahi.airlines.advice.constant.ErrorCodes;
import com.sipahi.airlines.advice.exception.InsufficientAmountException;
import com.sipahi.airlines.advice.exception.SeatNotAvailableException;
import com.sipahi.airlines.enums.FlightSeatStatus;
import com.sipahi.airlines.enums.TestAccountType;
import com.sipahi.airlines.persistence.model.dto.AccountDto;
import com.sipahi.airlines.persistence.model.request.BuySeatRequest;
import com.sipahi.airlines.persistence.mongo.document.FlightSeatDocument;
import com.sipahi.airlines.persistence.mysql.entity.AircraftEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightAmountEntity;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private FlightService flightService;
    @Mock
    private FlightSeatService flightSeatService;
    @Mock
    private AccountService accountService;
    @Mock
    private FlightAmountService flightAmountService;
    @Mock
    private AircraftService aircraftService;

    @Test
    void shouldThrowSeatNotAvailableException() {
        BuySeatRequest request = BuySeatRequest.builder()
                .flightNumber(RandomStringUtils.randomNumeric(3))
                .seatNo("4D")
                .build();

        FlightEntity flight = new FlightEntity();
        flight.setId(RandomUtils.nextLong());

        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setEconomyRowCount(5);
        aircraft.setVipRowCount(2);
        aircraft.setSeatsPerRow(10);

        FlightAmountEntity flightAmount = new FlightAmountEntity();
        flightAmount.setEconomy(new BigDecimal("500"));
        flightAmount.setVip(new BigDecimal("1000"));

        FlightSeatDocument flightSeat = new FlightSeatDocument();
        flightSeat.setSeatNo(request.getSeatNo());
        flightSeat.setFlightSeatStatus(FlightSeatStatus.SOLD);

        when(flightService.getLockedDetail(request.getFlightNumber())).thenReturn(flight);
        when(flightSeatService.getFlightSeats(flight.getId())).thenReturn(List.of(flightSeat));
        when(aircraftService.getDetailById(flight.getAircraftId())).thenReturn(aircraft);
        when(flightAmountService.findByFlightId(flight.getId())).thenReturn(flightAmount);

        SeatNotAvailableException exception = assertThrows(
                SeatNotAvailableException.class,
                () -> paymentService.buySeat(request, TestAccountType.ACCOUNT_1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(ErrorCodes.SEAT_NOT_FOUND, exception.getCode());
        assertEquals("Seat Not Available!", exception.getMessage());
    }

    @Test
    void shouldThrowInsufficientAmountException() {
        BuySeatRequest request = BuySeatRequest.builder()
                .flightNumber(RandomStringUtils.randomNumeric(3))
                .seatNo("4D")
                .build();

        FlightEntity flight = new FlightEntity();
        flight.setId(RandomUtils.nextLong());

        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setEconomyRowCount(5);
        aircraft.setVipRowCount(2);
        aircraft.setSeatsPerRow(10);

        FlightAmountEntity flightAmount = new FlightAmountEntity();
        flightAmount.setEconomy(new BigDecimal("500000000"));
        flightAmount.setVip(new BigDecimal("1000000000"));

        AccountDto account = new AccountDto();
        account.setAmount(flightAmount.getEconomy().subtract(new BigDecimal("1")));

        when(flightService.getLockedDetail(request.getFlightNumber())).thenReturn(flight);
        when(flightSeatService.getFlightSeats(flight.getId())).thenReturn(List.of());
        when(aircraftService.getDetailById(flight.getAircraftId())).thenReturn(aircraft);
        when(flightAmountService.findByFlightId(flight.getId())).thenReturn(flightAmount);
        when(accountService.getAccount(TestAccountType.ACCOUNT_1)).thenReturn(account);

        InsufficientAmountException exception = assertThrows(
                InsufficientAmountException.class,
                () -> paymentService.buySeat(request, TestAccountType.ACCOUNT_1));
        assertEquals(ErrorCodes.INSUFFICIENT_AMOUNT, exception.getCode());
        assertEquals("Insufficient Amount!", exception.getMessage());
    }

    @Test
    void shouldBuySeat() {
        BuySeatRequest request = BuySeatRequest.builder()
                .flightNumber(RandomStringUtils.randomNumeric(3))
                .seatNo("4D")
                .build();

        FlightEntity flight = new FlightEntity();
        flight.setId(RandomUtils.nextLong());
        flight.setFlightNumber(RandomStringUtils.randomNumeric(3));

        AircraftEntity aircraft = new AircraftEntity();
        aircraft.setEconomyRowCount(5);
        aircraft.setVipRowCount(2);
        aircraft.setSeatsPerRow(10);

        FlightAmountEntity flightAmount = new FlightAmountEntity();
        flightAmount.setEconomy(new BigDecimal("500000000"));
        flightAmount.setVip(new BigDecimal("1000000000"));

        AccountDto account = new AccountDto();
        account.setAmount(flightAmount.getEconomy().add(new BigDecimal("1")));

        when(flightService.getLockedDetail(request.getFlightNumber())).thenReturn(flight);
        when(flightSeatService.getFlightSeats(flight.getId())).thenReturn(List.of());
        when(aircraftService.getDetailById(flight.getAircraftId())).thenReturn(aircraft);
        when(flightAmountService.findByFlightId(flight.getId())).thenReturn(flightAmount);
        when(accountService.getAccount(TestAccountType.ACCOUNT_1)).thenReturn(account);

        paymentService.buySeat(request, TestAccountType.ACCOUNT_1);

        verify(flightSeatService).saveForBuy(flight.getId(), flight.getFlightNumber(), request.getSeatNo());
        verify(accountService).updateAccount(account.getId(), account.getAmount().subtract(flightAmount.getEconomy()));
    }
}