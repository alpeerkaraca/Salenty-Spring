package com.salenty.repositories;

import com.salenty.model.Order;
import com.salenty.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderId(Integer orderId);
    Order findOrderByBuyer(User buyer);
    List<Order> findOrdersByBuyer(User buyer);
}
