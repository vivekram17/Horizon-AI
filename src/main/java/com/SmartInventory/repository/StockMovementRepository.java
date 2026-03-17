package com.SmartInventory.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SmartInventory.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>{

    @Query("""
        SELECT ABS(COALESCE(SUM(sm.changeAmount),0))
        FROM StockMovement sm
        WHERE sm.product.id = :productId
        AND sm.type = 'SALE'
        AND sm.timestamp >= :startDate
        """)
    Double findSalesLast90Days(
        @Param("productId") Long productId,
        @Param("startDate") LocalDateTime startDate
    );


    @Query("""
        SELECT COUNT(sm)
        FROM StockMovement sm
        WHERE sm.type = 'SALE'
        """)
    Long countSales();

}