package com.csh.fuelpriceinsight.fuelservice.dto;

import com.csh.fuelpriceinsight.fuelservice.model.FuelPriceHistory;

import java.time.Instant;

public record FuelPriceResponse(
        double price,
        String formatted,
        String currency,
        String code,
        String type,
        String unit,
        Instant createdAt) {

    public static FuelPriceResponse from(OilPriceApiResponse oilPriceApiResponse) {
        OilPriceApiResponse.Data data = oilPriceApiResponse.data();
        return new FuelPriceResponse(
                data.price(),
                data.formatted(),
                data.currency(),
                data.code(),
                data.type(),
                data.unit(),
                data.created_at());
    }

    public static FuelPriceResponse from(FuelPriceHistory fuelPriceHistory) {
        return new FuelPriceResponse(
                fuelPriceHistory.getPrice(),
                fuelPriceHistory.getFormatted(),
                fuelPriceHistory.getCurrency(),
                fuelPriceHistory.getCode(),
                fuelPriceHistory.getType(),
                fuelPriceHistory.getUnit(),
                fuelPriceHistory.getCreatedAt());
    }
}