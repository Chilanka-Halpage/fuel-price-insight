package com.csh.fuelpriceinsight.fuelservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class FuelPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double price;
    private String formatted;
    private String currency;
    private String code;
    private Instant createdAt;
    private Instant updatedAt;
    private String type;
    private String unit;
    private String source;
}
