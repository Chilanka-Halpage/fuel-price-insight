package com.csh.fuelpriceinsight.fuelservice.dto;

import java.time.Instant;

public record OilPriceApiResponse(
        String status,
        Data data
) {
    public record Data(
            double price,
            String formatted,
            String currency,
            String code,
            Instant created_at,
            Instant updated_at,
            String type,
            String unit,
            String source
    ) {}
}