package com.SmartInventory.dto;

public class LowStockAlertDTO {

    private String productName;
    private int quantity;

    public LowStockAlertDTO(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}