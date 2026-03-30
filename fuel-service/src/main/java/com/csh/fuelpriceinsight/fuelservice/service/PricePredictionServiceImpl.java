package com.csh.fuelpriceinsight.fuelservice.service;

import com.csh.fuelpriceinsight.fuelservice.dto.FuelPriceResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricePredictionServiceImpl implements PricePredictionService {
    private static final String CACHE_NAME = "predictedPrice";

    private final WebClient aiServiceWebClient;
    private final FuelPriceService fuelPriceService;
    private final CacheManager cacheManager;

    @Override
    @CircuitBreaker(name = "pricePrediction", fallbackMethod = "getPredictedPriceFallback")
    @Retry(name = "pricePrediction")
    public String getPredictedPrice(String code, String Frequency) {
        try {
            FuelPriceResponse cachedFuelPrice = fuelPriceService.getCachedFuelPrice(code);
            String pricePrediction = aiServiceWebClient.get()
                    .uri("/api/ai-service/predicted-price/{type}/{currentPrice}/{frequencyType}", code, cachedFuelPrice.price(), Frequency)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String key = code + "~" + Frequency;
            updateCache(key, pricePrediction);

            return pricePrediction;
        } catch (Exception e) {
            log.error("Error in getting predicted fuel price - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getPredictedPriceFallback(String code, String frequency, Throwable ex) {
        log.warn("Circuit breaker fallback triggered for code : {}. Reason: {}",
                code, ex.getMessage());
        String key = code + "~" + frequency;
        String cacheData = getFromCache(key);
        if (cacheData != null) {
            return cacheData;
        }

        log.error("Data is not available for [{} - {}]", code, frequency);
        throw new RuntimeException("AI Service currently not available");
    }

    private String getFromCache(String key) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(key);
            if (wrapper != null) {
                log.info("Returning cached data for key: {}", key);
                return (String) wrapper.get();
            }
        }

        return null;
    }

    private void updateCache(String key, String value) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(key, value);
        }
    }
}
