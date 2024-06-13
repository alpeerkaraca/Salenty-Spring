package com.salenty.controllers;

import com.salenty.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.salenty.services.ProductService;
import com.salenty.services.UserService;

@Controller()
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;



}
