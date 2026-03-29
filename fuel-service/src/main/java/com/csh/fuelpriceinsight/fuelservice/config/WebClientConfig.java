package com.csh.fuelpriceinsight.fuelservice.config;

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

//    @Bean
//    @LoadBalanced
//    public WebClient.Builder loadBalancedWebClientBuilder() {
//        return WebClient.builder();
//    }
//
//    @Bean
//    public WebClient playerServiceWebClient(WebClient.Builder loadBalancedWebClientBuilder, @Value("${cricket.stats.baseurl}") String baseUrl) {
//        return loadBalancedWebClientBuilder.baseUrl(baseUrl).build();
//    }

    @Bean
    public WebClient OilApiServiceWebClient(WebClient.Builder defaultWebClientBuilder, @Value("${oilapi.uri}") String oilPriceApiUrl, @Value("${oilapi.key}") String oilPriceApiKey) {
        return defaultWebClientBuilder.baseUrl(oilPriceApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Token " + oilPriceApiKey)
                .build();
    }

}
