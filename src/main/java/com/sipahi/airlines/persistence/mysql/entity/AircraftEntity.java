package com.sipahi.airlines.persistence.mysql.entity;

import com.sipahi.airlines.enums.AircraftStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "aircraft")
@EntityListeners(AuditingEntityListener.class)
public class AircraftEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer passengerCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AircraftStatus status;

    @CreatedDate
    private LocalDateTime createdDate;
}
