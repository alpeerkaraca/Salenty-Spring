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
import com.salenty.model.*;
import com.salenty.services.*;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    public ProductController(ProductService productService, UserService userService, CartService cartService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable("productId") int productId, Model model) {
        User user = userService.findByUserName("alpeerkaracatest");
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
            Cart cart = cartService.getCartByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cartService.saveCart(cart);
            }

            Product product = productService.getAllProducts().stream().filter(p -> p.getProductId() == productId).findFirst().orElse(null);
            if (product != null) {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(1);
                item.setCart(cart);
                cartService.addItemToCart(cart, item);
                System.out.println("Product added to cart: " + product.getProductName());
            }

            // Sepet bilgilerini modele ekleme
            cart = cartService.getCartByUser(user);
            if (cart != null) {
                model.addAttribute("cartItemCount", cart.getItems().size());
            } else {
                model.addAttribute("cartItemCount", 0);
            }
        } else {
            System.err.println("User not found: alpeerkaracatest");
        }

        return "redirect:/product/" + productId;
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("coverImage") MultipartFile coverImage, @RequestParam("firstImage") MultipartFile firstImage, @RequestParam("secondImage") MultipartFile secondImage, @RequestParam("thirdImage") MultipartFile thirdImage) {
        // Save product and images
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            productService.saveProduct(product, coverImage, auth, firstImage, secondImage, thirdImage);
        } catch (Exception e) {
            System.err.println("Error while saving product: " + e.getMessage());
        }
        return "redirect:/account/myproducts";
    }

}
