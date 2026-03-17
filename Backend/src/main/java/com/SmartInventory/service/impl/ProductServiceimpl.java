package com.SmartInventory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SmartInventory.entity.Product;
import com.SmartInventory.exception.DuplicateResourceException;
import com.SmartInventory.exception.InsufficientStockException;
import com.SmartInventory.exception.ResourceNotFoundException;
import com.SmartInventory.repository.ProductRepository;
import com.SmartInventory.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceimpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Product create(Product product) {

        repository.findBySku(product.getSku())
                .ifPresent(p -> {
                    throw new DuplicateResourceException(
                            "SKU already exists: " + product.getSku());
                });

        return repository.save(product);
    }

    @Override
    public List<Product> getAll(){
        return repository.findAll();
    }

    @Override
    public List<Product> getLowStock(){
        return repository.findByQuantityLessThan(10);
    }

    public List<Product> getLowStock(int threshold){
        return repository.findByQuantityLessThan(threshold);
    }

    @Override
    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Product not found with ID: " + id)
                );
    }

    @Override
    public void reduceStock(Long productId, int quantity) {

        Product product = getById(productId);

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Only " + product.getQuantity() + " units available."
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        repository.save(product);
    }

    @Override
    public void increaseStock(Long productId, int quantity){

        Product product = getById(productId);

        product.setQuantity(product.getQuantity() + quantity);

        repository.save(product);
    }
}