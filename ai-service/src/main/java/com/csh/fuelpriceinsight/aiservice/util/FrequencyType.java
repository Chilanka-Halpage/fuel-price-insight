package com.csh.fuelpriceinsight.aiservice.util;

public enum FrequencyType {
    DAY("by tomorrow"),
    WEEK("in 1 week time"),
    TWO_WEEK("in 2 week time"),
    THREE_WEEK("in 3 week time"),
    MONTH("in 1 month time");

    private final String description;

    FrequencyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
