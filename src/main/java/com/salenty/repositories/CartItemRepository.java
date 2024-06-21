package com.salenty.repositories;

import com.salenty.model.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salenty.model.CartItem;

import java.util.List;

import com.salenty.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> getCartItemsByCart(Cart cart);
    CartItem findByCartAndProductId(Cart cart, Integer productId);
    void deleteCartItemByCart(Cart cart);
    void deleteByCartAndProductId(Cart cart, Integer productId);
}
