package com.SmartInventory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SmartInventory.entity.Order;
import com.SmartInventory.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestParam Long productId,
                            @RequestParam int quantity) {

        return orderService.placeOrder(productId, quantity);
    }

    @GetMapping("/sales/last30days")
    public List<Map<String,Object>> getSalesLast30Days(){
        return orderService.getLast30DaysSales();
    }
}