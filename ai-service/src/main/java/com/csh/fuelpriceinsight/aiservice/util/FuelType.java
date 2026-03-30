package com.csh.fuelpriceinsight.aiservice.util;

public enum FuelType {
    WTI_USD("West Texas Intermediate Crude Oil"),
    BRENT_CRUDE_USD("Brent Crude Oil"),
    NATURAL_GAS_USD("Henry Hub Natural Gas"),
    DIESEL_USD("Ultra Low Sulfur Diesel"),
    HEATING_OIL_USD("Heating Oil No. 2"),
    JET_FUEL_USD("Jet Fuel (Kerosene)"),
    GASOLINE_USD("RBOB Gasoline"),
    COAL_USD("Thermal Coal (Newcastle)");

    private final String description;

    FuelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
