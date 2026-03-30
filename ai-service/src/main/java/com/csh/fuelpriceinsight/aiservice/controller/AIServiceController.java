package com.csh.fuelpriceinsight.aiservice.controller;

import com.csh.fuelpriceinsight.aiservice.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai-service")
public class AIServiceController {

    private final GeminiService geminiService;

    @GetMapping("/predicted-price/{type}/{currentPrice}/{frequencyType}")
    public ResponseEntity<String> getPlayerStats(@PathVariable String type, @PathVariable  double currentPrice, @PathVariable  String frequencyType) {
        return ResponseEntity.ok(geminiService.getPredictedFuelPrice(type, currentPrice, frequencyType));
    }
}
