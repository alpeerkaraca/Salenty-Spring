package com.salenty.controllers;


import com.salenty.model.*;
import com.salenty.repositories.CartItemRepository;
import com.salenty.services.CartService;
import com.salenty.services.OrderService;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, CartService cartService, CartItemRepository cartItemRepository) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
    }

    @PostMapping("/order")
    public String order(Order order) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        Cart cart = cartService.getCartByUser(user);
        List<CartItem> cartItems = cart.getItems();
        List<Product> products = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            products.add(productService.getProductById(cartItem.getProductId()));
        }

        order.setBuyer(user);
        order.setProduct(products);
        order.setOrderStatus(Status.ONAY_BEKLIYOR);
        System.out.println("Order Verildi");

        System.out.println("Order Details\n" + order.toString());
        cartItems.forEach(cartItemRepository::delete);
        orderService.saveOrder(order);


        return "redirect:/homepage";
    }
}
