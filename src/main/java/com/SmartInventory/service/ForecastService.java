package com.SmartInventory.service;

import java.util.List;

import com.SmartInventory.dto.ForecastHistoryDTO;

public interface ForecastService {

    List<ForecastHistoryDTO> getAllForecastHistory();

    List<ForecastHistoryDTO> getProductForecastHistory(Long productId);

    List<ForecastHistoryDTO> getSupplierForecastHistory(String supplierEmail);
}