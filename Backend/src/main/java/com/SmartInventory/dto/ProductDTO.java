package com.SmartInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String sku;
    private int quantity;
    private double price;
}