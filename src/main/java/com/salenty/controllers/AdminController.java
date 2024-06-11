package com.salenty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.salenty.services.ProductService;
import com.salenty.services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller()
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    @GetMapping("/account/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("request", "usr");
        System.out.println("Userliste gidiyoruz....: " + model.getAttribute("request"));
        return "account";
    }

    @GetMapping("/account/orders")
    public String ordersList(Model model) {
//        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("request", "ordr");
        System.out.println("Userliste gidiyoruz....: " + model.getAttribute("request"));
        return "account";
    }

    @GetMapping("/account/my-products")
    public String myProducts(Model model) {
        model.addAttribute("products", productService.getProductBySellerId(1));
        model.addAttribute("request", "prd");
        return "account";
    }


    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/manage/userlist";
    }

    @GetMapping("/manage/productlist")
    public String productList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("auth: " + auth.getPrincipal().toString());
//        model.addAttribute("products", productService.getProductBySellerId(userService.getUserId(auth.getPrincipal().toString())));
        model.addAttribute("products", productService.getProductBySellerId(1));
        return "my-products";
    }


}
