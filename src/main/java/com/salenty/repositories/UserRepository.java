package com.salenty.repositories;

import com.salenty.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUsersByUserName(String userName);
    List<User> findAll();

    User findUserByUserId(int userId);
}
