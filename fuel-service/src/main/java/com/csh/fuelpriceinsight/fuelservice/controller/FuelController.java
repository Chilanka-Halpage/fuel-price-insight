package com.csh.fuelpriceinsight.fuelservice.controller;

import com.csh.fuelpriceinsight.fuelservice.dto.FuelPriceResponse;
import com.csh.fuelpriceinsight.fuelservice.service.FuelPriceService;
import com.csh.fuelpriceinsight.fuelservice.service.PricePredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
public class FuelController {

    private final FuelPriceService fuelPriceService;
    private final PricePredictionService pricePredictionService;

    @GetMapping("/price")
    public ResponseEntity<FuelPriceResponse> getPrice(@RequestParam("code") String code) {
        FuelPriceResponse price = fuelPriceService.getPrice(code);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/price-history")
    public ResponseEntity<List<FuelPriceResponse>> getPriceHistory(@RequestParam("code") String code) {
        List<FuelPriceResponse> priceHistory = fuelPriceService.getFuelPriceHistoryByCode(code);
        return ResponseEntity.ok(priceHistory);
    }

    @GetMapping("/price-prediction/{code}/{frequency}")
    public ResponseEntity<String> getPricePrediction(@PathVariable() String code, @PathVariable String frequency) {
        String predictedPrice = pricePredictionService.getPredictedPrice(code, frequency);
        return ResponseEntity.ok(predictedPrice);
    }
}
