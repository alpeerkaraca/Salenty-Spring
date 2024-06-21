package com.salenty.controllers;


import com.salenty.model.*;
import com.salenty.repositories.CartItemRepository;
import com.salenty.repositories.ProductRepository;
import com.salenty.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class GetOperationsController {

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    public GetOperationsController(ProductService productService, UserService userService, CategoryService categoryService, CartService cartService, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/homepage")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication status: " + auth.isAuthenticated());
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Credentials: " + auth.getCredentials());
        System.out.println("Authorities: " + auth.getAuthorities());
        System.out.println("Details: " + auth.getDetails());
        System.out.println("Name: " + auth.getName());

        List<Product> products = productService.getAllProducts();
        List<Product> productsWillSend = new ArrayList<>(12);
        for (int i = 0; i < products.size() && i < 13; i++) {
            productsWillSend.add(products.get(i));
        }


        model.addAttribute("products", productsWillSend);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartItemCount", cartItemRepository.getCartItemsByCart(cartService.getCartByUser(userService.findByUserName(auth.getName()))).size());
        return "/homepage";
    }


    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable("id") int id, Model model) {
        Product product = productService.getAllProducts().stream().filter(p -> p.getProductId() == id).findFirst()
                .orElse(null);

        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productDetailPage";
        } else {
            return "error"; // Not found page
        }
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Product> productsInCart = new ArrayList<>();
        User user = userService.findByUserName(auth.getName());
        model.addAttribute("categories", categoryService.getAllCategories());
        if (user != null && cartService.getCartByUser(user) != null) {

            cartService.getCartByUser(user).getItems().forEach(item -> {
                if(productRepository.findById(item.getProductId()).isPresent()) {
                    productsInCart.add(productRepository.findById(item.getProductId()).get());
                }
            });
            model.addAttribute("total", cartService.calculateTotal(cartService.getCartByUser(user)));
            model.addAttribute("items", cartService.getCartByUser(user).getItems());
            model.addAttribute("productsInCart", productsInCart);
        }
        return "cart";
    }

    @GetMapping("/account/{section}")
    public String account(@PathVariable("section") String section, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", auth.getName());

        switch (section) {
            case "users":
                model.addAttribute("users", userService.getAllUsers());
                break;
            case "myproducts":
                userService.findByUserName(auth.getName());
                model.addAttribute("orders", productService.getProductBySellerId(userService.findByUserName(auth.getName()).getUserId()));
                break;
            case "orders":
                model.addAttribute("orders", productService.getAllProducts());
                System.out.println("Orders: " + productService.getAllProducts());
                break;
            case "addProduct":
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("product", new Product());
                model.addAttribute("category", new Category());
                break;
            case "settings":
                model.addAttribute("user", userService.findByUserName(auth.getName()));
                break;
            case "allProducts":
                model.addAttribute("products", productService.getAllProducts());
                break;
            default:
                model.addAttribute("section", "orders");
                break;
        }

        return "account";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
        productService.deleteProductsByUserId(userId);
        return "redirect:/account/users";
    }

    @GetMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") int productId) {
        productService.deleteProduct(productId);
        return "redirect:/account/myproducts";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        List<Product> productsInCart = new ArrayList<>();

        cartService.getCartByUser(user).getItems().forEach(item -> {
            if(productRepository.findById(item.getProductId()).isPresent()) {
                productsInCart.add(productRepository.findById(item.getProductId()).get());
            }
        });

        model.addAttribute("userInfo", user);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("items", cartService.getCartByUser(user).getItems());
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("total", cartService.calculateTotal(cartService.getCartByUser(user)));
        model.addAttribute("order", new Order());
        System.out.println("Options: " + Arrays.asList(Countries.values()));
        model.addAttribute("options", Arrays.asList(Countries.values()));
        return "checkout";
    }

}
