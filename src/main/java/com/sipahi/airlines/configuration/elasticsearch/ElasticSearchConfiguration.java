package com.sipahi.airlines.configuration.elasticsearch;

import com.sipahi.airlines.configuration.properties.ElasticSearchProperties;
import org.apache.http.HttpHost;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class ElasticSearchConfiguration {

    private final ElasticSearchProperties elasticSearchProperties;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        HttpHost httpHost = new HttpHost(elasticSearchProperties.getHost(), elasticSearchProperties.getPort());
        RestClient restClient = RestClient.builder(httpHost)
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
