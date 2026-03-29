package com.csh.fuelpriceinsight.fuelservice.repository;

import com.csh.fuelpriceinsight.fuelservice.model.FuelPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuelPriceRepository extends JpaRepository<FuelPriceHistory, Long> {
    Optional<FuelPriceHistory> findTopFuelPriceHistoryByCodeOrderByCreatedAtDesc(String code);
    List<FuelPriceHistory> findFuelPriceHistoryByCodeOrderByCreatedAtDesc(String code);
}
