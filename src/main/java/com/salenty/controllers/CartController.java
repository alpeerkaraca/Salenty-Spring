package com.salenty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.salenty.services.CartService;
import com.salenty.services.UserService;
import com.salenty.model.User;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        User user = userService.findByUserName("alpeerkaracatest"); // Burayı dinamik hale getirin
        if (user != null) {
            model.addAttribute("cartItems", cartService.getCartByUser(user).getItems());
        }
        return "/fragments/cart";
    }
}
