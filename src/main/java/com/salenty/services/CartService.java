package com.salenty.services;

import com.salenty.model.Cart;
import com.salenty.model.CartItem;
import com.salenty.model.User;
import com.salenty.repositories.CartRepository;
import com.salenty.repositories.CartItemRepository;
import com.salenty.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public CartItem getCartItemByProductId(Cart cart, int productId) {
        if(cart == null || cart.getItems() == null || cart.getItems().isEmpty())
            return null;
        return cart.getItems().stream()
               .filter(item -> item.getProductId() == productId)
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

    public void addItemToCart(Cart cart, Integer productId) {
        CartItem existingItem = getCartItemByProductId(cart, productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
        }
    }

    public BigDecimal calculateTotal(Cart cart){
        List<CartItem> cartItems = cart.getItems();
        BigDecimal total = new BigDecimal(0);
        for (CartItem item : cartItems) {
            total = total.add(BigDecimal.valueOf(productRepository.findById(item.getProductId()).get().getProductPrice()).multiply(new BigDecimal(item.getQuantity())));
        }
        System.out.println("Cart Total: " + total);
        return total;
    }

    public void deleteCartByUser(User userById) {
        cartRepository.deleteByUser(userById);
    }
}
