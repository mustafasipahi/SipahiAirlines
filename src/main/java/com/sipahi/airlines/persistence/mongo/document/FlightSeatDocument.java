package com.sipahi.airlines.persistence.mongo.document;

import com.sipahi.airlines.enums.FlightSeatStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("FlightSeat")
public class FlightSeatDocument {

    @Id
    private String id;
    private Long flightId;
    private String seatNo;
    @Field(targetType = FieldType.STRING)
    private FlightSeatStatus flightSeatStatus;
    @CreatedDate
    private LocalDateTime createdDate;
}
