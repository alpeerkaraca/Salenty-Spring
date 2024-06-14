package com.salenty.repositories;

import com.salenty.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import com.salenty.model.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> getCartItemsByCart(Cart cart);
}
