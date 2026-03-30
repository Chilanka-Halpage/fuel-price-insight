package com.csh.fuelpriceinsight.fuelservice.service;

import com.csh.fuelpriceinsight.fuelservice.dto.FuelPriceResponse;
import com.csh.fuelpriceinsight.fuelservice.dto.OilPriceApiResponse;
import com.csh.fuelpriceinsight.fuelservice.model.FuelPriceHistory;
import com.csh.fuelpriceinsight.fuelservice.repository.FuelPriceRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuelPriceServiceImpl implements FuelPriceService {
    private static final String CACHE_NAME = "fuelPrice";

    private final WebClient OilApiServiceWebClient;
    private final CacheManager cacheManager;
    private final FuelPriceRepository fuelPriceRepository;

    @Override
    @CircuitBreaker(name = "fuelPrice", fallbackMethod = "getFuelPriceFallback")
    @Retry(name = "fuelPrice")
    public FuelPriceResponse getPrice(String code) {
        try {
            log.info("Calling get fuel price for code: {}", code);
            OilPriceApiResponse oilPriceApiResponse = OilApiServiceWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/prices/latest")
                            .queryParam("by_code", code)
                            .build())
                    .retrieve()
                    .bodyToMono(OilPriceApiResponse.class)
                    .block();

            FuelPriceResponse fuelPriceResponse = FuelPriceResponse.from(oilPriceApiResponse);

            updateCache(code, fuelPriceResponse);
            updateDB(oilPriceApiResponse);

            return fuelPriceResponse;
        } catch (Exception e) {
            log.error("Error in getting fuel price - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FuelPriceResponse> getFuelPriceHistoryByCode(String code) {
        try {
            log.info("Calling get fuel price history details for code: {}", code);

            List<FuelPriceHistory> history = fuelPriceRepository.findFuelPriceHistoryByCodeOrderByCreatedAtDesc(code);
            return history.stream().map(FuelPriceResponse::from).toList();
        } catch (Exception e) {
            log.error("Error in getting fuel price history - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public FuelPriceResponse getCachedFuelPrice(String code) {
        FuelPriceResponse cacheResponse = getFromCache(code);
        if (cacheResponse == null) {
            FuelPriceResponse dbResponse = getFromDB(code);
            if (dbResponse != null) {
                updateCache(code, dbResponse);
                return  dbResponse;
            }
        } else {
            return cacheResponse;
        }
        log.error("Data is not available for code : {}", code);
        throw new RuntimeException("Data is not available for code : " + code);
    }

    private FuelPriceResponse getFuelPriceFallback(String code, Throwable ex) {
        log.warn("Circuit breaker fallback triggered for code : {}. Reason: {}",
                code, ex.getMessage());
        return getCachedFuelPrice(code);
    }

    private void updateCache(String key, FuelPriceResponse response) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(key, response);
        }
    }

    private void updateDB(OilPriceApiResponse response) {
        try {
            OilPriceApiResponse.Data data = response.data();
            FuelPriceHistory fuelPriceHistory = FuelPriceHistory.builder()
                    .price(data.price())
                    .formatted(data.formatted())
                    .currency(data.currency())
                    .code(data.code())
                    .createdAt(data.created_at())
                    .updatedAt(data.updated_at())
                    .type(data.type())
                    .unit(data.unit())
                    .build();
            fuelPriceRepository.save(fuelPriceHistory);
        } catch (Exception e) {
            log.error("Error in saving fuel price - {}", e.getMessage());
        }
    }

    private FuelPriceResponse getFromCache(String key) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(key);
            if (wrapper != null) {
                log.info("Returning cached data for code: {}", key);
                return (FuelPriceResponse) wrapper.get();
            }
        }

        return null;
    }

    private FuelPriceResponse getFromDB(String code) {
        try {
            Optional<FuelPriceHistory> fuelPrice = fuelPriceRepository.findTopFuelPriceHistoryByCodeOrderByCreatedAtDesc(code);
            if (fuelPrice.isPresent()) {
                log.info("Returning DB data for code: {}", code);
                return FuelPriceResponse.from(fuelPrice.get());
            }
            return null;
        } catch (Exception e) {
            log.error("Error in retrieving fuel price from DB - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
