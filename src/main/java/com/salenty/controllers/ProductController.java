package com.salenty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.salenty.model.Product;
import com.salenty.model.ProductDetail;
import com.salenty.services.CategoryService;
import com.salenty.services.ProductDetailService;
import com.salenty.services.ProductService;
import com.salenty.services.UserService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/homepage")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            java.util.List<Product> products = productService.getAllProducts();
            for (int i = 0; i < products.size(); i++) {
                if (i < 12)
                    products.get(i).setSellerName(userService.getUserById(products.get(i).getSellerId()).getUserName());
                else {
                    products.remove(i);
                }
            }
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

    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable("id") int id, Model model) {
        Product product = productService.getAllProducts().stream().filter(p -> p.getProductId() == id).findFirst().orElse(null);
        ProductDetail productDetail = productDetailService.getProductDetailByProductId(id);

        if (product != null && productDetail != null) {
            product.setSellerName(userService.getUserById(product.getSellerId()).getUserName()); // Seller name set here
            model.addAttribute("product", product);
            model.addAttribute("productDetail", productDetail);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productDetailPage";
        } else {
            return "error/404"; // Not found page
        }
    }
    
    

}
