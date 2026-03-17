package com.SmartInventory.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.SmartInventory.dto.ForecastDTO;
import com.SmartInventory.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceimpl implements EmailService {

    private final JavaMailSender mailSender;

    // LOW STOCK ALERT
    @Override
    public void sendLowStockAlert(String to, String productName, int quantity, int threshold, ForecastDTO data) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Low Stock Alert - " + productName);
        message.setText("""
                WARNING: Low Stock Detected

                Product: %s
                Available Quantity: %d
                Minimum Required: %d
                AI Suggestion: %s

                And your product has been Restock according to the Data.
                """.formatted(productName, quantity, threshold, data));

        mailSender.send(message);
    }

    // MONTHLY DEMAND FORECAST EMAIL
    @Override
    public void sendForecastReport(String to, String productName, String forecast) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Monthly Demand Forecast - " + productName);
        message.setText("""
                Inventory Demand Forecast Report

                Product: %s

                AI Forecast:
                %s

               And your product has been Restock according to the Data.
                """.formatted(productName, forecast));

        mailSender.send(message);
    }
}