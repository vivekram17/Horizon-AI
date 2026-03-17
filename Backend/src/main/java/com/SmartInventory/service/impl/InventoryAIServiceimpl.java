package com.SmartInventory.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.SmartInventory.dto.ForecastDTO;
import com.SmartInventory.service.InventoryAIService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryAIServiceimpl implements InventoryAIService {

	private final ChatClient chatClient;
	private final ObjectMapper objectMapper;

	@Override
	public ForecastDTO generateReorderSuggestion(String productName, int currentQty, int minThreshold) {

		try {

			String prompt = """
					You are an inventory management AI.

					IMPORTANT RULES:
					- Return ONLY valid JSON
					- Do NOT include explanations
					- Response must start with { and end with }
					- Don't add any extra details only the data given in format

					Product: %s
					Current Quantity: %d
					Minimum Threshold: %d

					JSON FORMAT:

					{"month":"Month1","predictedDemand":number,"remainingStock":number,"recommendedRestock":Integer,"productName":"Product Name"}

					Suggest how many units should be reordered for the next 30 days.
					"""
					.formatted(productName, currentQty, minThreshold);

			String aiResponse = chatClient.prompt(prompt).call().content();

			aiResponse = aiResponse.replace("```json", "").replace("```", "").trim();

			// fix decimals like 20.
			aiResponse = aiResponse.replaceAll("(\\d+)\\.(?=[,}])", "$1.0");

			return objectMapper.readValue(aiResponse, ForecastDTO.class);

		} catch (Exception e) {

			log.error("AI reorder suggestion failed", e);

			return new ForecastDTO("Month1", 0.0, (double) currentQty, 0, productName);
		}
	}

	@Override
	public List<ForecastDTO> generateDemandForecast(String productName,
	                                                Integer salesHistory,
	                                                Double currentStock,
	                                                Double minThreshold) {

	    LocalDate now = LocalDate.now();
	    Double avgMonthlySales = salesHistory * 30.0;

	    String months = now.plusMonths(1).getMonth().name() + ", " +
	                    now.plusMonths(2).getMonth().name() + ", " +
	                    now.plusMonths(3).getMonth().name();

	    try {

	        return chatClient.prompt()

	                .system("""
	                        You are a strict JSON generator.

	                        Rules:
	                        - Return ONLY a JSON array
	                        - No markdown
	                        - Start with [ and end with ]
	                        - Use exact month names provided
	                        - Return objects exactly for number of months
	                        """)

	                .user(u -> u.text("""
	                        Forecast demand for {productName}

	                        Monthly Avg Sales: {avgSales}
	                        Current Stock: {stock}
	                        Threshold: {threshold}

	                        Target Months:
	                        {months}

	                        Return EXACTLY this structure:

	                        [
	                          {{
	                            "month": "April",
	                            "predictedDemand": 120,
	                            "remainingStock": 300,
	                            "recommendedRestock": 0,
	                            "productName": "{productName}"
	                          }}
	                        ]
	                        """)
	                        .param("productName", productName)
	                        .param("avgSales", avgMonthlySales)
	                        .param("stock", currentStock)
	                        .param("threshold", minThreshold)
	                        .param("months", months))

	                .options(ChatOptions.builder()
	                        .temperature(0.0)
	                        .maxTokens(5000)
	                        .build())

	                .call()
	                .entity(new ParameterizedTypeReference<List<ForecastDTO>>() {});

	    } catch (Exception e) {
	        log.error("AI Forecast failed for {} ", productName, e);
	        return Collections.emptyList();
	    }
	}

}
