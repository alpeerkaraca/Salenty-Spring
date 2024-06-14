package com.salenty.repositories;

import com.salenty.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface ProductRepository extends  JpaRepository<Product, java.lang.Integer>{
    List<Product> findBySellerId(int id);


    void deleteBySellerId(int sellerId);
}
