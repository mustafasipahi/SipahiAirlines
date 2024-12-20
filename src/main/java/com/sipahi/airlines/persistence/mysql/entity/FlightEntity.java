package com.sipahi.airlines.persistence.mysql.entity;

import com.sipahi.airlines.enums.FlightStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "flight")
@EntityListeners(AuditingEntityListener.class)
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long aircraftId;

    @Column(nullable = false, updatable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime flightDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
