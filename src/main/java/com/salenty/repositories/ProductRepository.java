package com.salenty.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salenty.model.Product;

public interface ProductRepository extends JpaRepository<Product, java.lang.Integer> {
    List<Product> findBySellerId(int id);
    Product findByProductId(int id);
    void deleteBySellerId(int sellerId);

    List<Product> findByProductNameContainingIgnoreCase(String name);
}
