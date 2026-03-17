package com.SmartInventory.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SmartInventory.entity.Order;
import com.SmartInventory.entity.Product;
import com.SmartInventory.entity.StockMovement;
import com.SmartInventory.exception.InsufficientStockException;
import com.SmartInventory.exception.ResourceNotFoundException;
import com.SmartInventory.repository.OrderRepository;
import com.SmartInventory.repository.ProductRepository;
import com.SmartInventory.repository.StockMovementRepository;
import com.SmartInventory.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceimpl implements OrderService{

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockMovementRepository stockMovementRepository;

    @Override
    @Transactional
    public Order placeOrder(Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        int before = product.getQuantity();

        if (before < quantity) {
            throw new InsufficientStockException("Not enough stock available");
        }

        int after = before - quantity;

        product.setQuantity(after);
        productRepository.save(product);

        Order order = new Order();
        order.setProduct(product);
        order.setQuantityOrdered(quantity);
        order.setTotalPrice(product.getPrice() * quantity);
        order.setOrderTime(LocalDateTime.now());

        orderRepository.save(order);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setQuantityBefore(before);
        movement.setQuantityAfter(after);
        movement.setChangeAmount(-quantity);
        movement.setType("SALE");
        movement.setTimestamp(LocalDateTime.now());

        stockMovementRepository.save(movement);

        return order;
    }

    public List<Map<String, Object>> getLast30DaysSales() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        List<Object[]> results = orderRepository.getDetailedRecentSales(startDate);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("productName", row[1]);
            map.put("quantity", row[2]);
            map.put("totalPrice", row[3]);
            map.put("date", row[4]);
            response.add(map);
        }
        return response;
    }
}