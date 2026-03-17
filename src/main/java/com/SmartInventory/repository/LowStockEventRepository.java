package com.SmartInventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SmartInventory.entity.LowStockEvent;

public interface LowStockEventRepository extends JpaRepository<LowStockEvent, Long> {

    Optional<LowStockEvent> existsByProductIdAndResolvedFalse(Long productId);

    List<LowStockEvent> existsByResolvedFalse();

}