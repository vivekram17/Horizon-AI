package com.SmartInventory.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.SmartInventory.dto.ForecastHistoryDTO;
import com.SmartInventory.entity.ForecastReport;
import com.SmartInventory.repository.ForecastRepository;
import com.SmartInventory.service.ForecastService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;

    @Override
    public List<ForecastHistoryDTO> getAllForecastHistory() {

        return forecastRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ForecastHistoryDTO> getProductForecastHistory(Long productId) {

        return forecastRepository
                .findByProductIdOrderByGeneratedAtDesc(productId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ForecastHistoryDTO> getSupplierForecastHistory(String supplierEmail) {

        return forecastRepository
                .findBySupplierEmailOrderByGeneratedAtDesc(supplierEmail)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ForecastHistoryDTO mapToDTO(ForecastReport report) {

        return new ForecastHistoryDTO(
                report.getProduct().getId(),
                report.getSupplierEmail(),
                report.getAiForecast(),
                report.getGeneratedAt()
        );
    }
}