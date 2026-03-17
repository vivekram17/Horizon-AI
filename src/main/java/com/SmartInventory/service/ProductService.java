package com.SmartInventory.service;

import java.util.List;

import com.SmartInventory.entity.Product;

public interface ProductService {

	public Product create(Product product);

	public List<Product> getAll();

	public List<Product> getLowStock();

	public Product getById(Long id);

	public void reduceStock(Long productId, int quantity);

	void increaseStock(Long productId, int quantity);

}
