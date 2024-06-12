package com.salenty.repositories;

import com.salenty.model.Cart;
import com.salenty.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
