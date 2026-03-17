package com.SmartInventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastDTO {

    private String month;
    private Double predictedDemand;
    private Double remainingStock;
    private Integer recommendedRestock;
    private String productName;

}