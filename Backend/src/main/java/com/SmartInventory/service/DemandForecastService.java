package com.SmartInventory.service;

import java.util.List;

import com.SmartInventory.dto.ForecastDTO;

public interface DemandForecastService {

	public List<ForecastDTO> forecastDemand(String productName,
            Integer salesHistory,
            Double quantity,
            Double threshold);
	

String generateForecastForProduct(Long productId);

List<String> generateForecastForAllProducts();


String ReorderSuggestion(Long productId);
}