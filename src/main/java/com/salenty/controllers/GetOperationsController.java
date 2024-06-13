package com.salenty.controllers;


import com.salenty.model.Category;
import com.salenty.model.Product;
import com.salenty.model.ProductDetail;
import com.salenty.model.User;
import com.salenty.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GetOperationsController {

    private ProductService productService;

    private ProductDetailService productDetailService;

    private UserService userService;

    private CategoryService categoryService;

    private CartService cartService;

    public GetOperationsController(ProductService productService, ProductDetailService productDetailService, UserService userService, CategoryService categoryService, CartService cartService) {
        this.productService = productService;
        this.productDetailService = productDetailService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.cartService = cartService;
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/homepage")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication status: " + auth.isAuthenticated());
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Credentials: " + auth.getCredentials());
        System.out.println("Authorities: " + auth.getAuthorities());
        System.out.println("Details: " + auth.getDetails());
        System.out.println("Name: " + auth.getName());

        java.util.List<Product> products = productService.getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            if (i < 12)
                products.get(i).setSellerName(userService.getUserById(products.get(i).getSellerId()).getUsername());
            else {
                products.remove(i);
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("user", auth.getName());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/homepage";
    }

    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable("id") int id, Model model) {
        Product product = productService.getAllProducts().stream().filter(p -> p.getProductId() == id).findFirst()
                .orElse(null);
        ProductDetail productDetail = productDetailService.getProductDetailByProductId(id);

        if (product != null && productDetail != null) {
            product.setSellerName(userService.getUserById(product.getSellerId()).getUsername()); // Seller name set here
            model.addAttribute("product", product);
            model.addAttribute("productDetail", productDetail);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productDetailPage";
        } else {
            return "error"; // Not found page
        }
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        User user = userService.findByUserName("alpeerkaracatest"); // Burayı dinamik hale getirin
        if (user != null) {
            model.addAttribute("cartItems", cartService.getCartByUser(user).getItems());
        }
        return "/fragments/cart";
    }

    @GetMapping("/account/{section}")
    public String account(@PathVariable("section") String section, Model model) {

        switch (section) {
            case "users":
                model.addAttribute("users", userService.getAllUsers());
                break;
            case "myproducts":
                model.addAttribute("orders", productService.getAllProducts());
                break;
            case "orders":
                model.addAttribute("orders", productService.getAllProducts());
                break;
            case "addProduct":
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("product", new Product());
                model.addAttribute("category", new Category());
                model.addAttribute("productDetail", new ProductDetail());

                break;
            case "settings":
                model.addAttribute("user", userService.getUserById(1));
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
