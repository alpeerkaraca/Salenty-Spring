package com.salenty.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.salenty.model.Product;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;
import com.salenty.services.CartService;
import com.salenty.model.Cart;
import com.salenty.model.CartItem;
import com.salenty.model.User;

@Controller
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable("productId") int productId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cartService.saveCart(cart);
            }

            Product product = productService.getProductById(productId);
            if (product != null) {
                cartService.addItemToCart(cart, productId);
            }

            cart = cartService.getCartByUser(user);
            if (cart != null && cart.getItems() != null) {
                model.addAttribute("cartItemCount", cart.getItems().size());
            } else {
                model.addAttribute("cartItemCount", 0);
            }
        }

        return "redirect:/product/" + productId;
    }

    @PostMapping("/cart/update-quantity/{productId}/increase")
    public String increaseQuantity(@PathVariable("productId") int productId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null) {
                CartItem item = cartService.getCartItemByProductId(cart, productId);
                if (item != null) {
                    item.setQuantity(item.getQuantity() + 1);
                    cartService.saveCart(cart);
                }
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update-quantity/{productId}/decrease")
    public String decreaseQuantity(@PathVariable("productId") int productId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null) {
                CartItem item = cartService.getCartItemByProductId(cart, productId);
                if (item != null && item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    cartService.saveCart(cart);
                }
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove-from-cart/{productId}")
    public String removeItemFromCart(@PathVariable("productId") int productId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null) {
                cartService.removeItemFromCart(cart, productId);
            }
        }
        return "redirect:/cart";
    }
}
