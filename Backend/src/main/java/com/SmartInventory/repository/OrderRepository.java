package com.SmartInventory.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SmartInventory.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
			SELECT COALESCE(SUM(o.totalPrice),0)
			FROM Order o
			WHERE o.orderTime >= :startDate
			""")
	Double getSalesSince(LocalDateTime startDate);

	@Query("""
		    SELECT o.id, p.name, o.quantityOrdered, o.totalPrice, o.orderTime
		    FROM Order o
		    JOIN o.product p
		    WHERE o.orderTime >= :startDate
		    ORDER BY o.orderTime DESC
		    """)
		List<Object[]> getDetailedRecentSales(LocalDateTime startDate);

	@Query("""
			SELECT COALESCE(SUM(o.totalPrice),0)
			FROM Order o
			WHERE DATE(o.orderTime) = CURRENT_DATE
			""")
	Double getTodaySales();

	long count();

}