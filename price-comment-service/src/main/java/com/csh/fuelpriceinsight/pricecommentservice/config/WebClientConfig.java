package com.csh.fuelpriceinsight.pricecommentservice.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient commentManagerServiceWebClient(WebClient.Builder loadBalancedWebClientBuilder,
                                                    @Value("${comment-manager-service.baseurl}") String baseUrl,
                                                    ObservationRegistry observationRegistry) {
        return loadBalancedWebClientBuilder
                .baseUrl(baseUrl)
                .observationRegistry(observationRegistry)
                .build();
    }

}
