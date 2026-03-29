package com.csh.fuelpriceinsight.fuelservice.service;

import com.csh.fuelpriceinsight.fuelservice.dto.FuelPriceResponse;

import java.util.List;

public interface FuelPriceService {
    FuelPriceResponse getPrice(String code);
    List<FuelPriceResponse> getFuelPriceHistoryByCode(String code);
    FuelPriceResponse getCachedFuelPrice(String code);
}
