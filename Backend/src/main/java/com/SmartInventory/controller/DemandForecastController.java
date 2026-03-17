package com.SmartInventory.controller;

import org.springframework.web.bind.annotation.*;

import com.SmartInventory.service.DemandForecastService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai-forecast")
@RequiredArgsConstructor
public class DemandForecastController {

    private final DemandForecastService demandForecastService;

    // AI forecast for a product
    @GetMapping("/{productId}")
    public String forecastProduct(@PathVariable Long productId) {

        return demandForecastService.generateForecastForProduct(productId);
    }

    // AI forecast for all products
    @GetMapping("/run")
    public String forecastAllProducts() {

        demandForecastService.generateForecastForAllProducts();

        return "Forecast generated for all products";
    }
}