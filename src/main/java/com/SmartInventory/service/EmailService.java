package com.SmartInventory.service;

import com.SmartInventory.dto.ForecastDTO;

public interface EmailService {
	public void sendLowStockAlert(String to, String productName, int quantity, int threshold, ForecastDTO suggestion);

	void sendForecastReport(String to, String productName, String forecast);
}
