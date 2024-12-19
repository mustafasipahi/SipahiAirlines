package com.sipahi.airlines.persistence.specification;

import com.sipahi.airlines.persistence.entity.FlightEntity;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightSpecification {

    public static Specification<FlightEntity> filter(FlightSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getFlightName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getFlightName() + "%"));
            }
            if (request.getFlightNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("flightNumber"), request.getFlightNumber()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
