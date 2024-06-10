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
    System.out.println("Getting all products");
    System.out.println(productRepository.findAll());
        return productRepository.findAll();
    }


}
