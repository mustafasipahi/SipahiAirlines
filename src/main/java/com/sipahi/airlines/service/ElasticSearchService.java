package com.sipahi.airlines.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.sipahi.airlines.advice.exception.ElasticEventSaveException;
import com.sipahi.airlines.converter.ElasticSearchConverter;
import com.sipahi.airlines.converter.FlightConverter;
import com.sipahi.airlines.persistence.model.dto.FlightDto;
import com.sipahi.airlines.persistence.model.event.FlightElasticSearchEvent;
import com.sipahi.airlines.persistence.model.request.FlightSearchRequest;
import com.sipahi.airlines.persistence.mysql.entity.FlightEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sipahi.airlines.advice.constant.ElastickSeaarch.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final FlightConverter flightConverter;

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
            Query query = getQuery(request);
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(FLIGHT_INDEX)
                    .query(query)
                    .sort(so -> so.field(f -> f.field(FLIGHT_DATE).order(SortOrder.Desc)))
                    .size(DEFAULT_SEARCH_SIZE)
                    .source(ss -> ss.fetch(true)));
            SearchResponse<Map<String, Object>> searchResponse = elasticsearchClient.search(searchRequest, (Type) Map.class);
            return searchResponse.hits().hits()
                    .stream()
                    .filter(hit -> Objects.nonNull(hit.source()))
                    .map(hit -> flightConverter.constructDto(hit.source()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("An error occurred while searching flights: {}", request, ex);
            return Collections.emptyList();
        }
    }

    public void deleteAllDocument(String indexName) {
        try {
            boolean exists = elasticsearchClient.indices().exists(ExistsRequest.of(s -> s.index(indexName))).value();
            if (exists) elasticsearchClient.indices().delete(DeleteIndexRequest.of(s -> s.index(indexName)));
        } catch (IOException e) {
            log.info("An error occurred while delete all", e);
        }
    }

    private Query getQuery(FlightSearchRequest request) {
        if (StringUtils.isBlank(request.getFlightNumber())) {
            return Query.of(q -> q.matchAll(ma -> ma));
        }
        return Query.of(q -> q.matchPhrasePrefix(mp -> mp.field(FLIGHT_NUMBER).query(request.getFlightNumber())));
    }
}
