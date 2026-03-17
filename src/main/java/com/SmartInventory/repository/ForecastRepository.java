package com.SmartInventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SmartInventory.entity.ForecastReport;

public interface ForecastRepository extends JpaRepository<ForecastReport, Long>{

    List<ForecastReport> findByProductIdOrderByGeneratedAtDesc(Long productId);

    List<ForecastReport> findBySupplierEmailOrderByGeneratedAtDesc(String supplierEmail);

}