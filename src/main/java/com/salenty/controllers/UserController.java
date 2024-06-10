package com.salenty.controllers;

import com.salenty.model.User;
import com.salenty.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        user.setUserStatus(false);
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/user-login")
    public String login(User user) {
        System.out.println("User: " + user.getUserName());
        boolean isUserExist = userService.findByUserName(user.getUserName()) != null;
        if (isUserExist) {
            User userFromDB = userService.findByUserName(user.getUserName());
            if (userService.checkPassword(userFromDB, user.getUserPassword())) {
                return "redirect:/homepage";
            }
        }
        return "redirect:/login?error=true";
    }





}