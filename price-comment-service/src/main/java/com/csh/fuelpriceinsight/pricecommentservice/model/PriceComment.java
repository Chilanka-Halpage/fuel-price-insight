package com.csh.fuelpriceinsight.pricecommentservice.model;

import lombok.Data;

import java.time.Instant;

@Data
public class PriceComment {
    private String description;
    private String userId;
    private String userName;
    private Instant lastUpdatedAt;
}
