package com.csh.fuelpriceinsight.aiservice.service;

import com.csh.fuelpriceinsight.aiservice.service.dto.GeminiRequest;
import com.csh.fuelpriceinsight.aiservice.service.dto.GeminiResponse;
import com.csh.fuelpriceinsight.aiservice.util.FrequencyType;
import com.csh.fuelpriceinsight.aiservice.util.FuelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GeminiServiceImpl implements GeminiService {

    private final WebClient geminiServiceWebClient;

    @Override
    public String getPredictedFuelPrice(String type, double currentPrice, String frequencyType) {
        try {
            GeminiRequest requestBody = getRequestBody(type, currentPrice, frequencyType);
            GeminiResponse response = geminiServiceWebClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

            return response.candidates().getFirst().content().parts().getFirst().text();
        } catch (Exception e) {
            log.error("Error in retrieving data form Gemini for [{}, {}] - {}",type, frequencyType, e.getMessage());
            throw e;
        }
    }

    private GeminiRequest getRequestBody(String type, double currentPrice, String frequencyType) {
        String prompt = String.format("""
                Give the price for %s, whose current price is %s, %s in following json format. Give SHORT description specifying the reasons and possible price volatility throughout the above specified period.
                {
                    "predictedPrice": "",
                    "description": ""
                }
                Please give the SHORT response using EXACT above format and dont include ```json``` and ** in response
                }""", FuelType.valueOf(type).getDescription(), currentPrice, FrequencyType.valueOf(frequencyType).getDescription());

        return new GeminiRequest(
                List.of(new GeminiRequest.Content(
                                List.of(new GeminiRequest.Part(prompt))
                        )
                ));
    }


}
