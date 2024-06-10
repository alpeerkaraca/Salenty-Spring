package com.salenty.controllers;

import com.salenty.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.salenty.services.ProductService;
import com.salenty.services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller()
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    @GetMapping("/manage/userlist")
    public String userList(Model model) {

        model.addAttribute("users", userService.getAllUsers());
        return "userlist";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/manage/userlist";
    }


}
