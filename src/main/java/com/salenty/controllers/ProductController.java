package com.salenty.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.salenty.model.Product;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("coverImage") MultipartFile coverImage, @RequestParam("firstImage") MultipartFile firstImage, @RequestParam("secondImage") MultipartFile secondImage, @RequestParam("thirdImage") MultipartFile thirdImage) throws Exception {
        productService.saveProduct(product, coverImage, firstImage, secondImage, thirdImage);
        return "redirect:/account/myproducts";
    }
}
