package com.salenty.controllers;

import com.salenty.model.Product;
import com.salenty.model.User;
import com.salenty.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.salenty.services.ProductService;
import com.salenty.services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller()
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/account/{section}")
    public String account(@PathVariable("section") String section, Model model) {

        List<User> users = userService.getAllUsers();
        List<Product> orders = productService.getAllProducts();

        switch (section) {
            case "users":
                model.addAttribute("users", userService.getAllUsers());
                break;
            case "myproducts":
                model.addAttribute("orders", orders);
                break;
            case "orders":
                model.addAttribute("orders", orders);

                break;
            case "settings":
                break;
            default:
                model.addAttribute("section", "orders");
                break;
        }
        return "account";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
        productService.deleteProduct(userId);
        return "redirect:/account/users";
    }

    @GetMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") int productId) {
        productService.deleteProduct(productId);
        return "redirect:/account/myproducts";
    }

}
