package com.csh.fuelpriceinsight.fuelservice.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Bean
    public WebClient.Builder defaultWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient aiServiceWebClient(WebClient.Builder loadBalancedWebClientBuilder,
                                        @Value("${ai-service.baseurl}") String baseUrl,
                                        ObservationRegistry observationRegistry) {
        return loadBalancedWebClientBuilder
                .baseUrl(baseUrl)
                .observationRegistry(observationRegistry)
                .build();
    }

    @Bean
    public WebClient OilApiServiceWebClient(WebClient.Builder defaultWebClientBuilder,
                                            @Value("${oilapi.uri}") String oilPriceApiUrl,
                                            @Value("${oilapi.key}") String oilPriceApiKey,
                                            ObservationRegistry observationRegistry) {
        return defaultWebClientBuilder.baseUrl(oilPriceApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Token " + oilPriceApiKey)
                .observationRegistry(observationRegistry)
                .build();
    }

}
