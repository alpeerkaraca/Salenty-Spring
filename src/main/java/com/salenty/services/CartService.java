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

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public CartItem getCartItemByProductId(Cart cart, int productId) {
        return cart.getItems().stream()
               .filter(item -> item.getProduct().getProductId() == productId)
               .findFirst()
               .orElse(null);
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void removeItemFromCart(Cart cart, int productId) {
        CartItem item = getCartItemByProductId(cart, productId);
        if (item != null) {
            cartItemRepository.delete(item);
        }
    }

    public void addItemToCart(Cart cart, Product product) {
        CartItem existingItem = getCartItemByProductId(cart, product.getProductId());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void emptyCart(Cart cart) {
        cartItemRepository.deleteCartItemByCart(cart);
        cart.setItems(new ArrayList<CartItem>());
        cartRepository.save(cart);
    }
}
