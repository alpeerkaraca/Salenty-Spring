package com.salenty.controllers;


import com.salenty.model.*;
import com.salenty.services.CartService;
import com.salenty.services.OrderService;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PostMapping("/give-order")
    public String createOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Order order = new Order();
        User buyer = userService.findByUserName(authentication.getName());
        List<CartItem> buyersCart = cartService.getCartByUser(buyer).getItems();
        List<Product> orderedProducts = new ArrayList<Product>();

        order.setOrderStatus(Status.HAZIRLANIYOR);
        order.setBuyer(buyer);
        buyersCart.forEach(item -> {
            orderedProducts.add(item.getProduct());
        });

        order.setProduct(orderedProducts);

        System.out.println("Order Created");
        System.out.println(order.toString());
        System.out.println("Ordered Products: " + order.getProduct().toString());

        orderService.createOrder(order);
        cartService.emptyCart(cartService.getCartByUser(buyer));
        return "redirect:/homepage";
    }

    @GetMapping("/test")
    public String testFunc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User buyer = userService.findByUserName(authentication.getName());
        Order order = orderService.getOrderByBuyer(buyer);

        if (order == null) {
            System.out.println("Order is null");
            order = new Order();
        }

        System.out.println(order.toString());
        return "give-order";
    }
}
