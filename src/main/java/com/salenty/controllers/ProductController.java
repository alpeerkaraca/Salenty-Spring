package com.salenty.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salenty.model.Category;
import com.salenty.model.Product;
import com.salenty.services.CategoryService;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    
    @Autowired
    private CategoryService categoryService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("coverImage") MultipartFile coverImage, @RequestParam("firstImage") MultipartFile firstImage, @RequestParam("secondImage") MultipartFile secondImage, @RequestParam("thirdImage") MultipartFile thirdImage) throws Exception {
        productService.saveProduct(product, coverImage, firstImage, secondImage, thirdImage);
        return "redirect:/account/myproducts";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("search") String search, Model model) {
        List<Product> products = productService.searchProductsByName(search);
        model.addAttribute("products", products);
        
        // Add categories to the model
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        return "homepage"; // Arama sonuçlarını ana sayfada gösteriyoruz
    }
}
