package com.SmartInventory.service;

import java.util.List;

import com.SmartInventory.dto.ForecastDTO;

public interface InventoryAIService {
	public ForecastDTO generateReorderSuggestion(String productName, int currentQty, int minThreshold);

	List<ForecastDTO> generateDemandForecast(String productName, Integer salesHistory, Double currentStock, Double minThreshold);
}
