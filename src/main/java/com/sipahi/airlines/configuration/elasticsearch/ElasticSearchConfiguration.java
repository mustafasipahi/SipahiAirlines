package com.sipahi.airlines.configuration.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.sipahi.airlines.advice.exception.ElasticSearchConfigException;
import com.sipahi.airlines.configuration.properties.ElasticSearchProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ElasticSearchConfiguration {

    private static final String ELASTIC_SEARCH_NODE_SEPARATOR = ",";
    private static final String ELASTIC_SEARCH_SCHEME_PATH = "://";
    private final ElasticSearchProperties elasticSearchProperties;

    @Bean
    public ElasticsearchClient client() {
        ElasticsearchTransport transport = new RestClientTransport(
                restClient(), new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean(destroyMethod = "close")
    public RestClient restClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, getUsernamePasswordCredentials());
        List<HttpHost> hosts = getHosts();
        SSLContext sslContext = createIgnoreCertSSLContext();
        return RestClient.builder(hosts.toArray(new HttpHost[0]))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                                .setSSLContext(sslContext)
                                .setSSLHostnameVerifier((hostname, session) -> true))
                .build();
    }

    private UsernamePasswordCredentials getUsernamePasswordCredentials() {
        return new UsernamePasswordCredentials(
                elasticSearchProperties.getUsername(),
                elasticSearchProperties.getPassword()
        );
    }

    private List<HttpHost> getHosts() {
        String scheme = elasticSearchProperties.getScheme();
        return Arrays.stream(elasticSearchProperties.getHost().split(ELASTIC_SEARCH_NODE_SEPARATOR))
                .map(urlString -> {
                    try {
                        URI uri = new URI(scheme + ELASTIC_SEARCH_SCHEME_PATH + urlString);
                        return new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                    } catch (Exception e) {
                        throw new ElasticSearchConfigException("Invalid URL: " + urlString);
                    }
                }).collect(Collectors.toList());
    }

    private SSLContext createIgnoreCertSSLContext() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new ElasticSearchConfigException("Failed to create SSL context");
        }
    }
}
