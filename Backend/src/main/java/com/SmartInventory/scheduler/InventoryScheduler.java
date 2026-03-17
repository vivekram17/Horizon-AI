package com.SmartInventory.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.SmartInventory.dto.ForecastDTO;
import com.SmartInventory.entity.LowStockEvent;
import com.SmartInventory.entity.Product;
import com.SmartInventory.repository.LowStockEventRepository;
import com.SmartInventory.repository.ProductRepository;
import com.SmartInventory.repository.StockMovementRepository;
import com.SmartInventory.service.DemandForecastService;
import com.SmartInventory.service.EmailService;
import com.SmartInventory.service.InventoryAIService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryScheduler {

    private final ProductRepository productRepository;
    private final LowStockEventRepository eventRepository;
    private final StockMovementRepository stockMovementRepository;
    private final EmailService emailService;
    private final InventoryAIService aiService;
    private final DemandForecastService demandForecastService;
    private final ObjectMapper objectMapper;

    // LOW STOCK DETECTION
    @Scheduled(fixedRate = 500000)
    @Transactional
    public void detectLowStockProducts() {

        log.info("🔍 Running scheduled low-stock check...");

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
        	

            if (product.getQuantity() < product.getMinThreshold()) {
            	
            	log.info(product.getSupplier().getEmail());
            	
	                boolean exists =
	                        eventRepository.existsByProductIdAndResolvedFalse(product.getId()).isPresent();
                log.info(exists+"");

                if (exists) {
                	
                	System.out.println(product.getName());
                    LowStockEvent event = LowStockEvent.builder()
                            .product(product)
                            .productName(product.getName())
                            .sku(product.getSku())
                            .currentQuantity(product.getQuantity())
                            .minThreshold(product.getMinThreshold())
                            .resolved(false)
                            .detectedAt(LocalDateTime.now())
                            .build();

                    eventRepository.save(event);

                    ForecastDTO suggestion = aiService.generateReorderSuggestion(
                            product.getName(),
                            product.getQuantity(),
                            product.getMinThreshold()
                    );

                    emailService.sendLowStockAlert(
                            product.getSupplier().getEmail(),
                            product.getName(),
                            product.getQuantity(),
                            product.getMinThreshold(),
                            suggestion
                    );

                    log.warn("⚠ Low stock event saved for {}", product.getName());
                }
            }
        }

        log.info("✅ Low-stock check completed.");
    }

    @Scheduled(cron = "0 0 2 1 * ?") 
    @Transactional
    public void runMonthlyDemandForecast() {

        log.info("📊 Running monthly demand forecasting...");

        List<Product> products = productRepository.findAll();

        for (Product product : products) {

            Integer salesLast90Days = (int) Math.round(
                    stockMovementRepository.findSalesLast90Days(
                            product.getId(),
                            LocalDateTime.now().minusDays(90)
                    ));

            List<ForecastDTO> forecastList = demandForecastService.forecastDemand(
                    product.getName(),
                    salesLast90Days,
                    (double) product.getQuantity(),
                    (double) product.getMinThreshold()
            );

            String forecastJson;
            try {
                forecastJson = objectMapper.writeValueAsString(forecastList);
            } catch (Exception e) {
                log.error("Failed to serialize forecast for {}", product.getName(), e);
                forecastJson = "[]"; // fallback
            }

            emailService.sendForecastReport(
                    product.getSupplier().getEmail(),
                    product.getName(),
                    forecastJson
            );

            log.info("📈 Forecast generated for {}", product.getName());
        }

        log.info("✅ Monthly forecasting completed.");
    }
}
