package com.csh.fuelpriceinsight.aiservice.service;

public interface GeminiService {
    String getPredictedFuelPrice(String type, double currentPrice, String frequencyType);
}
