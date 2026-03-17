package com.SmartInventory.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForecastHistoryDTO {

    private Long productId;

    private String supplierEmail;

    private String forecast;

    private LocalDateTime generatedAt;
}