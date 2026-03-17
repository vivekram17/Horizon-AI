package com.SmartInventory.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.SmartInventory.dto.ForecastDTO;
import com.SmartInventory.entity.ForecastReport;
import com.SmartInventory.entity.Product;
import com.SmartInventory.repository.ForecastRepository;
import com.SmartInventory.repository.ProductRepository;
import com.SmartInventory.repository.StockMovementRepository;
import com.SmartInventory.service.DemandForecastService;
import com.SmartInventory.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemandForecastServiceImpl implements DemandForecastService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ForecastRepository forecastRepository;
    private final InventoryAIServiceimpl aiService;
    private final ObjectMapper objectMapper;
    private final ProductService ps;
    @Override
    public List<ForecastDTO> forecastDemand(String productName,
                                            Integer salesHistory,
                                            Double quantity,
                                            Double threshold) {

        // Call AI service to generate forecast
        List<ForecastDTO> forecast = aiService.generateDemandForecast(
                productName,
                salesHistory,
                quantity,
                threshold
        );

        return forecast;
    }

    @Override
    public String generateForecastForProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Double sales = stockMovementRepository.findSalesLast90Days(
                productId,
                LocalDateTime.now().minusDays(90)
        );

        if (sales == null) {
            sales = 0.0;
        }

        // Average daily sales
        Double avgDailySales = sales / 90.0;

        List<ForecastDTO> forecast = forecastDemand(
                product.getName(),
                avgDailySales.intValue(),
                (double) product.getQuantity(),
                (double) product.getMinThreshold()
        );
        
        int x = forecast.get(0).getRecommendedRestock();
        
       ps.increaseStock(productId, x);
        

        ForecastReport report = new ForecastReport();
        report.setProduct(product);
        report.setGeneratedAt(LocalDateTime.now());
        report.setSupplierEmail(product.getSupplier().getEmail());

        try {
            String forecastJson = objectMapper.writeValueAsString(forecast);
            report.setAiForecast(forecastJson);
        } catch (Exception e) {
            log.error("Failed to serialize forecast for product {}", product.getName(), e);
            report.setAiForecast("[]"); 
        }

        forecastRepository.save(report);

        return report.getAiForecast();
    }
    
    @Override
    public String ReorderSuggestion(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Double sales = stockMovementRepository.findSalesLast90Days(
                productId,
                LocalDateTime.now().minusDays(90)
        );

        if (sales == null) {
            sales = 0.0;
        }

        ForecastDTO forecast = aiService.generateReorderSuggestion(
                product.getName(),
                (int) product.getQuantity(),
                (int) product.getMinThreshold()
        );
        
        int x = forecast.getRecommendedRestock();
        
       ps.increaseStock(productId, x);
        

        ForecastReport report = new ForecastReport();
        report.setProduct(product);
        report.setGeneratedAt(LocalDateTime.now());
        report.setSupplierEmail(product.getSupplier().getEmail());

        try {
            String forecastJson = objectMapper.writeValueAsString(forecast);
            report.setAiForecast(forecastJson);
        } catch (Exception e) {
            log.error("Failed to serialize forecast for product {}", product.getName(), e);
            report.setAiForecast("[]"); 
        }

        return report.getAiForecast();
    }

    @Override
    public List<String> generateForecastForAllProducts() {
        List<Product> products = productRepository.findAll();
        List<String> result = new ArrayList<>();

        for (Product product : products) {
            try {
                Long productId = product.getId();
                Double sales = stockMovementRepository.findSalesLast90Days(
                        productId,
                        LocalDateTime.now().minusDays(90)
                );

                if (sales == null) sales = 0.0;
                Double avgDailySales = sales / 90.0;

                // 1. CALL THE AI (This is the slow part)
                List<ForecastDTO> forecast = forecastDemand(
                        product.getName(),
                        avgDailySales.intValue(),
                        (double) product.getQuantity(),
                        (double) product.getMinThreshold()
                );

                // 2. PROCESS BUSINESS LOGIC
                if (forecast != null && !forecast.isEmpty()) {
                    int x = forecast.get(0).getRecommendedRestock();
                    ps.increaseStock(productId, x);

                    ForecastReport report = new ForecastReport();
                    report.setProduct(product);
                    report.setGeneratedAt(LocalDateTime.now());
                    report.setSupplierEmail(product.getSupplier().getEmail());

                    try {
                        String forecastJson = objectMapper.writeValueAsString(forecast);
                        report.setAiForecast(forecastJson);
                    } catch (Exception e) {
                        log.error("Serialization failed for {}", product.getName(), e);
                        report.setAiForecast("[]");
                    }

                    forecastRepository.save(report);
                    result.add(report.getAiForecast());
                }

                // 3. THE FIX: Wait 5 seconds to avoid the 429 Quota Error.
                // This keeps you at ~12 requests per minute.
                log.info("AI success for {}. Waiting 5s for quota reset...", product.getName());
                Thread.sleep(5000); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Forecast loop interrupted", e);
                break; 
            } catch (Exception e) {
                // This prevents the "Coca Cola" error from stopping the "Pepsi" forecast
                log.error("Skipping product {} due to error: {}", product.getName(), e.getMessage());
                continue; 
            }
        }
        return result;
    }
}
