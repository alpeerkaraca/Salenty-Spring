package com.salenty.controllers;

import com.salenty.model.User;
import com.salenty.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthenticationController {

    private final UserService userService;


    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(User request) {
        System.out.println(request);
        userService.save(request);
        return "redirect:/login";
    }

    @PostMapping("login")
    public String login() {
        return "redirect:/homepage";
    }

    @PostMapping("/user/edit/{id}")
    public String updateUser(@PathVariable String id, User user) {

        User userFromDB = userService.getUserById(Integer.parseInt(id));
        user.setUserId(Integer.parseInt(id));
        user.setUserRole(userFromDB.getUserRole());

        if (user.getPassword().isEmpty()) {
            user.setPassword(userFromDB.getPassword());
        }

        userService.save(user);
        return "redirect:/account/users";
    }

    @PostMapping("update-me")
    public String updateMe(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Update me sent user\n" + user.toString());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User userFromDB = userService.findByUserName(auth.getName());
        System.out.println("Update me user from db\n" + userFromDB.toString());
        user.setUserRole(userFromDB.getUserRole());
        user.setUserId(userFromDB.getUserId());

        if (user.getPassword().isEmpty() || user.getPassword() == null || encoder.matches(user.getPassword(), userFromDB.getPassword())) {
            user.setPassword(userFromDB.getPassword());
        }
        userService.save(user);
        return "redirect:/login?updated=true";
    }




}