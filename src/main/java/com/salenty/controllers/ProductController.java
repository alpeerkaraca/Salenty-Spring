package com.salenty.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        List<Product> products = productService.searchProductsByName(search);
        model.addAttribute("products", products);

        // Add categories to the model
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("username", auth.getName());

        return "homepage"; // Arama sonuçlarını ana sayfada gösteriyoruz
    }

    @GetMapping("/category")
    public String getProductsByCategory(@RequestParam("id") int categoryId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Category id: " + categoryId);
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        model.addAttribute("products", products);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("username", auth.getName());
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", categoryId);

        return "homepage"; // Kategori sonuçlarını ana sayfada gösteriyoruz
    }

    @PostMapping("/product/edit/{id}")
    public String updateProduct(Product product, @PathVariable("id") int id , @RequestParam MultipartFile coverImage, @RequestParam MultipartFile firstImage, @RequestParam MultipartFile secondImage, @RequestParam MultipartFile thirdImage) throws IOException {
        System.out.println("Update product Before: " + product);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        product.setSellerId(userService.findByUserName(auth.getName()).getUserId());
        product.setProductId(id);
        product.setSellerName(auth.getName());
        System.out.println("Update product: " + product);
        productService.updateProduct(product, coverImage, firstImage, secondImage, thirdImage);
        return "redirect:/homepage";
    }
}
