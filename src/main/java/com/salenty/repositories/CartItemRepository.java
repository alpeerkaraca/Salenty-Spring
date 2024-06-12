package com.salenty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salenty.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
