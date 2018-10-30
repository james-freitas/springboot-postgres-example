package com.codeonblue.service;

import java.util.List;

import com.codeonblue.domain.Product;

public interface ProductService {

    List<Product> listAll();

    Product getById(Long id);

    Product save(Product product);

    void delete(Long id);

    Product update(Product product);
	

}
