package com.codeonblue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeonblue.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
