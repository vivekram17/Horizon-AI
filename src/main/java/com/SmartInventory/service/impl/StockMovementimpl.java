package com.SmartInventory.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.SmartInventory.repository.StockMovementRepository;
import com.SmartInventory.service.StockMovement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockMovementimpl implements StockMovement {
	
	private final StockMovementRepository stockMovementRepository;

    @Override
    public Double getLast90DaysSales(Long productId) {

        LocalDateTime startDate = LocalDateTime.now().minusDays(90);

        Double sales = stockMovementRepository
                .findSalesLast90Days(productId, startDate);

        return sales == null ? 0.0 : sales;
    }
}
