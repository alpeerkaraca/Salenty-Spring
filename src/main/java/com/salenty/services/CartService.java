package com.salenty.services;

import com.salenty.model.Cart;
import com.salenty.model.CartItem;
import com.salenty.model.User;
import com.salenty.repositories.CartRepository;
import com.salenty.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private EntityManager entityManager;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Transactional
    public void addItemToCart(Cart cart, CartItem item) {
        if (cart.getItems() == null) {
            cart.setItems(new java.util.ArrayList<>());
        }
        cart.getItems().add(item);
        cartItemRepository.save(item);
        cartRepository.save(cart);
        entityManager.flush();
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
