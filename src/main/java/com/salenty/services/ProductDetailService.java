package com.salenty.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salenty.model.ProductDetail;
import com.salenty.repositories.ProductDetailRepository;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public ProductDetail getProductDetailByProductId(int productId) {
        return productDetailRepository.findByProductProductId(productId);
    }
}
