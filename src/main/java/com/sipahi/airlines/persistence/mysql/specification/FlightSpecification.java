package com.sipahi.airlines.persistence.mysql.specification;

import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightSpecification {

    public static Specification<FlightEntity> filter(FlightSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getFlightNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("flightNumber"), request.getFlightNumber()));
            }
            predicates.add(criteriaBuilder.equal(root.get("status"), FlightStatus.AVAILABLE));
            predicates.add(criteriaBuilder.greaterThan(root.get("flightDate"), LocalDateTime.now()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
