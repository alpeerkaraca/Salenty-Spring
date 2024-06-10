package com.salenty.services;

import com.salenty.model.User;
import com.salenty.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public boolean checkPassword(User user, String password) {
        System.out.println("Password: " + password);
        System.out.println("User Password: " + user.getUserPassword());
        System.out.println("Matches: " + bCryptPasswordEncoder.matches(password, user.getUserPassword()));
        return bCryptPasswordEncoder.matches(password, user.getUserPassword());
    }

}
