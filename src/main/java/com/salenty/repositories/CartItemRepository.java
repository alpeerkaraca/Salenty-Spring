package com.salenty.repositories;

import com.salenty.model.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salenty.model.CartItem;

import java.util.List;

import com.salenty.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> getCartItemsByCart(Cart cart);
    CartItem findByCartAndProduct(Cart cart, Product product);
    void deleteCartItemByCart(Cart cart);
    void deleteByCartAndProduct(Cart cart, Product product);
}
