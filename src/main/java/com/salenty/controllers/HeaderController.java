package com.salenty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.salenty.model.User;
import com.salenty.model.Cart;
import com.salenty.services.CartService;
import com.salenty.services.UserService;

@Controller
public class HeaderController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addAttributes(Model model) {
        User user = userService.findByUserName("alpeerkaracatest");
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null) {
                model.addAttribute("cartItemCount", cart.getItems().size());
            } else {
                model.addAttribute("cartItemCount", 0);
            }
        } else {
            model.addAttribute("cartItemCount", 0);
        }
    }
}
