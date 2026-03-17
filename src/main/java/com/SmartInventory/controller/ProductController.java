package com.SmartInventory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.SmartInventory.entity.Product;
import com.SmartInventory.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public Product create(@RequestBody Product product){
        return service.create(product);
    }

    @GetMapping
    public List<Product> getAll(){
        return service.getAll();
    }

    @GetMapping("/low-stock")
    public List<Product> lowStock(){
        return service.getLowStock();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){
        return service.getById(id);
    }
}