package com.salenty.controllers;

import com.salenty.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller()
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/homepage")
    public String home(Model model) {
        System.out.println("Wait a sec, gettin products..");
        model.addAttribute("products", productService.getAllProducts());
        System.out.println("Got products!");
        return "homepage";
    }


}
