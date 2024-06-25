package com.salenty.services;

import com.salenty.model.Role;
import com.salenty.model.User;
import com.salenty.repositories.UserRepository;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(user.getUserRole() == null)
            user.setUserRole(Role.valueOf("USER"));

        userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findUsersByUsername(userName);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUsersByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
