package com.salenty.services;

import com.salenty.model.User;
import com.salenty.repositories.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        user.setUserRole("USER");
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findUsersByUserName(userName);
    }

    public int getUserId(String userName) {
        return Math.toIntExact(userRepository.findUsersByUserName(userName).getUserId());
    }

    public boolean checkPassword(User user, String password) {
        System.out.println("Password: " + password);
        System.out.println("User Password: " + user.getUserPassword());
        System.out.println("Matches: " + bCryptPasswordEncoder.matches(password, user.getUserPassword()));
        return bCryptPasswordEncoder.matches(password, user.getUserPassword());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int sellerId) {
        return userRepository.findUserByUserId(sellerId);
    }

    public void deleteUser(int userId) {
        userRepository.deleteById((long) userId);
    }
}
