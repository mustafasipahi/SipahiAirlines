package com.sipahi.airlines.persistence.mysql.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "flightAmount")
@EntityListeners(AuditingEntityListener.class)
public class FlightAmountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private BigDecimal economy;

    @Column(nullable = false)
    private BigDecimal vip;
}
