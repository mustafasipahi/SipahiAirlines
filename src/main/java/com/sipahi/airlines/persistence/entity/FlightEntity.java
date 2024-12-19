package com.sipahi.airlines.persistence.entity;

import com.sipahi.airlines.enums.FlightStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "flight")
@EntityListeners(AuditingEntityListener.class)
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime flightDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraftEntity;

    @ManyToMany
    @JoinTable(name = "flight_user",
               joinColumns = @JoinColumn(name = "flight_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> users = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
