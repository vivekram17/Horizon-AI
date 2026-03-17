package com.SmartInventory.service;

import java.util.List;
import java.util.Map;

import com.SmartInventory.entity.Order;

public interface OrderService {

	public Order placeOrder(Long productId, int quantity);

	List<Map<String, Object>> getLast30DaysSales();

}
