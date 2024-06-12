package com.salenty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

import com.salenty.model.*;
import com.salenty.services.*;

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

    @Autowired
    private CartService cartService;

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
        Product product = productService.getAllProducts().stream().filter(p -> p.getProductId() == id).findFirst()
                .orElse(null);
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

    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable("productId") int productId, Model model) {
        User user = userService.findByUserName("alpeerkaracatest");
        if (user != null) {
            System.out.println("User found: " + user.getUserName());
            Cart cart = cartService.getCartByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cartService.saveCart(cart);
            }
    
            Product product = productService.getAllProducts().stream()
                    .filter(p -> p.getProductId() == productId)
                    .findFirst()
                    .orElse(null);
            if (product != null) {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(1);
                item.setCart(cart);
                cartService.addItemToCart(cart, item);
                System.out.println("Product added to cart: " + product.getProductName());
            }
    
            // Sepet bilgilerini modele ekleme
            cart = cartService.getCartByUser(user);
            if (cart != null) {
                model.addAttribute("cartItemCount", cart.getItems().size());
            } else {
                model.addAttribute("cartItemCount", 0);
            }
        } else {
            System.err.println("User not found: alpeerkaracatest");
        }
    
        return "redirect:/product/" + productId;
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam("firstImage") MultipartFile firstImage,
            @RequestParam("secondImage") MultipartFile secondImage,
            @RequestParam("thirdImage") MultipartFile thirdImage,
            RedirectAttributes redirectAttributes) {
        // Save product and images
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            productService.saveProduct(product, coverImage, auth, firstImage, secondImage, thirdImage);
            redirectAttributes.addFlashAttribute("message", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding product: " + e.getMessage());
        }
        return "redirect:/account/myproducts";
    }
    
}
