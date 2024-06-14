package com.salenty.services;

import com.salenty.model.Cart;
import com.salenty.model.CartItem;
import com.salenty.model.Product;
import com.salenty.model.User;
import com.salenty.repositories.CartRepository;
import com.salenty.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public void addItemToCart(Cart cart, CartItem item) {

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<CartItem>());
        }

        cart.getItems().add(item);
        cartItemRepository.save(item);
        cartRepository.save(cart);
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Transactional
    public void emptyCart(Cart cart) {
        cartItemRepository.deleteCartItemByCart(cart);
        cart.setItems(new ArrayList<CartItem>());
        cartRepository.save(cart);
    }


}
