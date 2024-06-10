package com.salenty.repositories;

import com.salenty.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUsersByUserName(String userName);
}
