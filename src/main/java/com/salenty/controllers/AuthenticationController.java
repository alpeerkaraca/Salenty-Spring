package com.salenty.controllers;

import com.salenty.model.User;
import com.salenty.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthenticationController {

    private final UserService userService;


    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(User request) {
        userService.save(request);
        return "redirect:/login";
    }

    @PostMapping("update-user/{id}")
    public String putMethodName(@PathVariable String id, User user) {

        User userFromDB = userService.getUserById(Integer.parseInt(id));
        user.setUserId(Integer.parseInt(id));
        user.setUserRole(userFromDB.getUserRole());

        if (user.getPassword().isEmpty()) {
            user.setPassword(userFromDB.getPassword());
        }

        userService.save(user);
        return "redirect:/account/settings";
    }

}