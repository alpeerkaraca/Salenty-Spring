package com.salenty.services;


import com.salenty.model.Order;
import com.salenty.model.Product;
import com.salenty.model.User;
import com.salenty.repositories.OrderRepository;
import com.salenty.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductService productService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public Order getOrderByBuyer(User buyer) {
        return orderRepository.findOrderByBuyer(buyer);
    }

    public List<Order> getOrdersByBuyer(User buyer) {
        return orderRepository.findOrdersByBuyer(buyer);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void cancelOrder(Integer orderId) {
        orderRepository.deleteById(orderId);

    }
}
