package com.csh.fuelpriceinsight.fuelservice.config;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.spring.starter.embedded.InfinispanCacheConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class InfinispanConfig {

    @Bean
    public InfinispanCacheConfigurer cacheConfigurer() {
        return manager -> {
            org.infinispan.configuration.cache.Configuration config = new ConfigurationBuilder()
                    .expiration()
                    .lifespan(1, TimeUnit.HOURS)
                    .maxIdle(30, TimeUnit.MINUTES)
                    .memory()
                    .maxCount(1000)
                    .build();

            manager.defineConfiguration("fuelPrice", config);
        };
    }
}
