package com.salenty.services;

import com.salenty.model.Product;
import com.salenty.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductBySellerId(int id) {
        return productRepository.findBySellerId(id);
    }

    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

}
