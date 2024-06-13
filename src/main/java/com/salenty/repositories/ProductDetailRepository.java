package com.salenty.repositories;

import com.salenty.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    ProductDetail findByProductProductId(int productId);

}
