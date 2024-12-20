package com.sipahi.airlines.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.sipahi.airlines.advice.exception.ElasticEventSaveException;
import com.sipahi.airlines.converter.ElasticSearchConverter;
import com.sipahi.airlines.enums.FlightStatus;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.event.FlightElasticSearchEvent;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.sipahi.airlines.advice.constant.ElastickSeaarch.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public void saveFlightEvent(FlightEntity flightEntity) {
        FlightElasticSearchEvent elasticEvent = ElasticSearchConverter.toEvent(flightEntity);
        try {
            IndexRequest<FlightElasticSearchEvent> indexRequest = IndexRequest.of(i -> i
                    .id(elasticEvent.getFlightNumber())
                    .index(FLIGHT_INDEX)
                    .document(elasticEvent));
            elasticsearchClient.index(indexRequest);
            log.info("Created flight event index at elasticsearch... Flight Number : {}", flightEntity.getFlightNumber());
        } catch (Exception ex) {
            throw new ElasticEventSaveException(flightEntity.getFlightNumber());
        }
    }

    public List<FlightDto> search(FlightSearchRequest request) {
        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(FLIGHT_INDEX)
                    .query(q -> q.matchPhrasePrefix(mp -> mp.field(FLIGHT_NAME).query(request.getFlightName())))
                    .sort(so -> so.field(f -> f.field(FLIGHT_DATE).order(SortOrder.Desc)))
                    .size(DEFAULT_SEARCH_SIZE)
                    .source(ss -> ss.fetch(true)));
            SearchResponse<Map<String, Object>> searchResponse = elasticsearchClient.search(searchRequest, (Type) Map.class);
            return searchResponse.hits().hits()
                    .stream()
                    .filter(hit -> Objects.nonNull(hit.source()))
                    .map(hit -> constructUserDto(hit.source()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("An error occurred while searching flights: {}", request, ex);
            return Collections.emptyList();
        }
    }

    private FlightDto constructUserDto(Map<String, Object> elasticResultMap) {
        try {
            return FlightDto.builder()
                    .flightNumber((String) elasticResultMap.get("flightNumber"))
                    .name((String) elasticResultMap.get("name"))
                    .description((String) elasticResultMap.get("description"))
                    .flightDate(elasticResultMap.containsKey("flightDate") ?
                            LocalDateTime.parse((String) elasticResultMap.get("flightDate")) : null)
                    .status(elasticResultMap.containsKey("status") ?
                            FlightStatus.valueOf((String) elasticResultMap.get("status")) : null)
                    .build();
        } catch (Exception e) {
            log.error("Error while constructing FlightDto from result map: {}", elasticResultMap, e);
            throw new IllegalStateException("Failed to construct FlightDto", e);
        }
    }
}
