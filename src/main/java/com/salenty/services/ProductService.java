package com.salenty.services;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.salenty.repositories.CartItemRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.salenty.model.Product;
import com.salenty.repositories.ProductRepository;

@Service
public class ProductService {
    private static final String DEFAULT_IMAGE_URL = "https://i.ibb.co/fQh9FQM/empty.png";
    private final String IMGBB_API_KEY = "26f39d2645c430852c778370d45eb13f";

    @Autowired
    private ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    @Autowired
    private CartItemRepository cartItemRepository;

    public ProductService(ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductBySellerId(int id) {
        return productRepository.findBySellerId(id);
    }

    public void deleteProduct(int productId) {
        cartItemRepository.deleteCartItemByProductId(productId);
        productRepository.deleteById(productId);
    }

    public void deleteProductsByUserId(int userId) {
        List<Product> products = productRepository.findBySellerId(userId);
        for (Product product : products) {
            productRepository.deleteById(product.getProductId());
        }
    }

    public void saveProduct(Product product, MultipartFile coverImage, MultipartFile... images)
            throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        product.setSellerId(userService.findByUserName(auth.getName()).getUserId());
        product.setSellerName(auth.getName());

        product.getCategory().setCategoryName(categoryService.getCategoryById(product.getCategory().getCategoryId()));


        if (!coverImage.isEmpty()) {
            String coverImageUrl = uploadImage(coverImage);
            System.out.println("Cover Image Yüklendi: " + coverImageUrl);
            product.setProductImage(coverImageUrl);
        } else {
            product.setProductImage(DEFAULT_IMAGE_URL);
        }


        if (images != null && images.length > 0) {
            for (int i = 0; i < images.length && i < 3; i++) {
                if (!images[i].isEmpty()) {
                    String imageUrl = uploadImage(images[i]);
                    if (i == 0) {
                        product.setProductImage1(imageUrl);
                    } else if (i == 1) {
                        product.setProductImage2(imageUrl);
                    } else if (i == 2) {
                        product.setProductImage3(imageUrl);
                    }
                } else {
                    if (i == 0) {
                        product.setProductImage1(DEFAULT_IMAGE_URL);
                    } else if (i == 1) {
                        product.setProductImage2(DEFAULT_IMAGE_URL);
                    } else if (i == 2) {
                        product.setProductImage3(DEFAULT_IMAGE_URL);
                    }
                }
            }
        }

        if (images == null || images.length < 3) {
            if (images == null || images.length == 0 || images[0].isEmpty()) {
                product.setProductImage1(DEFAULT_IMAGE_URL);
            }
            if (images == null || images.length < 2 || images[1].isEmpty()) {
                product.setProductImage2(DEFAULT_IMAGE_URL);
            }
            if (images == null || images.length < 3 || images[2].isEmpty()) {
                product.setProductImage3(DEFAULT_IMAGE_URL);
            }
        }

        productRepository.save(product);

        System.out.println("Product saved successfully.");
    }


    public String uploadImage(MultipartFile image) throws IOException {
        String uploadUrl = "https://api.imgbb.com/1/upload";
        RestTemplate restTemplate = new RestTemplate();

        // Resize the image to fit within a 1200x1200 square, then pad it to be exactly 1200x1200
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(image.getInputStream())
                .size(1200, 1200)
                .keepAspectRatio(true)
                .outputFormat("jpg")
                .addFilter((img) -> {
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    int maxDim = Math.max(width, height);
                    BufferedImage squareImage = new BufferedImage(maxDim, maxDim, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = squareImage.createGraphics();
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, maxDim, maxDim);
                    g2.drawImage(img, (maxDim - width) / 2, (maxDim - height) / 2, null);
                    g2.dispose();
                    return squareImage;
                })
                .toOutputStream(outputStream);

        byte[] resizedImageBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", IMGBB_API_KEY);
        body.add("image", new ByteArrayResource(resizedImageBytes) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject);
        return jsonObject.getJSONObject("data").getString("url");
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId);
    }

    public void updateProduct(Product product, MultipartFile coverImage, MultipartFile firstImage, MultipartFile secondImage, MultipartFile thirdImage) throws IOException {
        Product productFromDB = productRepository.findById(product.getProductId()).orElse(null);

        if (productFromDB == null) {
            return;
        }

        product.setProductId(productFromDB.getProductId());
        product.setSellerId(productFromDB.getSellerId());
        product.setSellerName(productFromDB.getSellerName());
        product.setProductImage(productFromDB.getProductImage());
        product.setProductImage1(productFromDB.getProductImage1());
        product.setProductImage2(productFromDB.getProductImage2());
        product.setProductImage3(productFromDB.getProductImage3());

        if (!coverImage.isEmpty()) {
            String coverImageUrl = uploadImage(coverImage);
            System.out.println("Cover Image Yüklendi: " + coverImageUrl);
            product.setProductImage(coverImageUrl);
        }

        if (!firstImage.isEmpty()) {
            String imageUrl = uploadImage(firstImage);
            product.setProductImage1(imageUrl);
        }

        if (!secondImage.isEmpty()) {
            String imageUrl = uploadImage(secondImage);
            product.setProductImage2(imageUrl);
        }

        if (!thirdImage.isEmpty()) {
            String imageUrl = uploadImage(thirdImage);
            product.setProductImage3(imageUrl);
        }

        productRepository.save(product);
    }
}
