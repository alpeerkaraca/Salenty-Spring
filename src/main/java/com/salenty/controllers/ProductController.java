package com.salenty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.salenty.model.Product;
import com.salenty.services.CategoryService;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;

@Controller()
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/homepage")
    public String home(Model model, Model userModel, Model categoryModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Wait a sec, gettin products..");
        if (auth != null && auth.isAuthenticated()) {
            java.util.List<Product> products = productService.getAllProducts();

            for (int i = 0; i < products.size(); i++) {
                if (i < 12)
                    products.get(i).setSellerName(userService.getUserById(products.get(i).getSellerId()).getUserName());
                else{
                    products.remove(i);
                }
            }
            products.removeLast();

            model.addAttribute("products", products);
            model.addAttribute("user", auth.getName());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "homepage";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/allproducts")
    public String allProducts(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("user", auth.getName());
            return "allproducts";
        } else {
            return "redirect:/login";
        }
    }

//    @GetMapping("/product/{productId}")
//    public String product(Model model, @PathVariable("productId") int productId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated()) {
//            model.addAttribute("product", productService.getProductById(productId));
//            model.addAttribute("user", auth.getName());
//            return "product";
//        } else {
//            return "redirect:/login";
//        }
//    }

}
