package com.codeonblue.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.codeonblue.domain.Product;
import com.codeonblue.repository.ProductRepository;

@Service
public class DBService {
	
	private final ProductRepository productRepository;
	
	public DBService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void instantiateDatabase() {
		Product product1 = new Product(null, "Computador", new BigDecimal("2000.00"), null);
		productRepository.save(product1);
	}
}
