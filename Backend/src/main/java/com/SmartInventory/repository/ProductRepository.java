package com.SmartInventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SmartInventory.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByQuantityLessThan(int threshold);

    long countByQuantityLessThan(int threshold);

    List<Product> findByNameContainingIgnoreCase(String name);

}