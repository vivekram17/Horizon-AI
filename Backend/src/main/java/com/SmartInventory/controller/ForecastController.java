package com.SmartInventory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.SmartInventory.dto.ForecastHistoryDTO;
import com.SmartInventory.service.ForecastService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/forecast")
@RequiredArgsConstructor
public class ForecastController {

    private final ForecastService forecastService;

    // ALL FORECAST HISTORY
    @GetMapping("/all")
    public List<ForecastHistoryDTO> getAllForecastHistory() {
        return forecastService.getAllForecastHistory();
    }

    // FORECAST HISTORY BY PRODUCT
    @GetMapping("/product/{productId}")
    public List<ForecastHistoryDTO> getProductForecastHistory(@PathVariable Long productId) {
        return forecastService.getProductForecastHistory(productId);
    }

    // FORECAST HISTORY BY SUPPLIER
    @GetMapping("/supplier")
    public List<ForecastHistoryDTO> getSupplierForecastHistory(@RequestParam String email) {
        return forecastService.getSupplierForecastHistory(email);
    }
}