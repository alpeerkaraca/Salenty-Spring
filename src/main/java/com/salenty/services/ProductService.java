package com.salenty.services;

import com.salenty.model.Product;
import com.salenty.model.ProductDetail;
import com.salenty.repositories.ProductDetailRepository;
import com.salenty.repositories.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private static final String DEFAULT_IMAGE_URL = "https://i.ibb.co/fQh9FQM/empty.png";
    private final String IMGBB_API_KEY = "26f39d2645c430852c778370d45eb13f";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductDetailService productDetailService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductBySellerId(int id) {
        return productRepository.findBySellerId(id);
    }

    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }

    public void saveProduct(Product product, MultipartFile coverImage, Authentication auth, MultipartFile... images)
            throws Exception {
        // Set the seller details
        product.setSellerId(1);
        product.setSellerName(userService.getUserById(product.getSellerId()).getUsername());

        // Set the category name
        product.getCategory().setCategoryName(categoryService.getCategoryById(product.getCategory().getCategoryId()));

        // Create a new ProductDetail and set its fields
        ProductDetail productDetail = new ProductDetail();
        productDetail.setDescription("Default Description");
        productDetail.setProductSpecs("Default Specs");

        // Set the Product for the ProductDetail
        product.setProductDetail(productDetail);

        // Upload the cover image and set the URL
        if (!coverImage.isEmpty()) {
            String coverImageUrl = uploadImage(coverImage);
            System.out.println("Cover Image Yüklendi: " + coverImageUrl);
            product.setProductImage(coverImageUrl);
        } else {
            product.setProductImage(DEFAULT_IMAGE_URL);
        }

        // Upload other images and set their URLs
        if (images != null && images.length > 0) {
            for (int i = 0; i < images.length && i < 3; i++) {
                if (!images[i].isEmpty()) {
                    String imageUrl = uploadImage(images[i]);
                    if (i == 0) {
                        productDetail.setImage1(imageUrl);
                    } else if (i == 1) {
                        productDetail.setImage2(imageUrl);
                    } else if (i == 2) {
                        productDetail.setImage3(imageUrl);
                    }
                } else {
                    if (i == 0) {
                        productDetail.setImage1(DEFAULT_IMAGE_URL);
                    } else if (i == 1) {
                        productDetail.setImage2(DEFAULT_IMAGE_URL);
                    } else if (i == 2) {
                        productDetail.setImage3(DEFAULT_IMAGE_URL);
                    }
                }
            }
        }

        // Ensure all image fields are set to default if not provided
        if (images == null || images.length < 3) {
            if (images == null || images.length == 0 || images[0].isEmpty()) {
                productDetail.setImage1(DEFAULT_IMAGE_URL);
            }
            if (images == null || images.length < 2 || images[1].isEmpty()) {
                productDetail.setImage2(DEFAULT_IMAGE_URL);
            }
            if (images == null || images.length < 3 || images[2].isEmpty()) {
                productDetail.setImage3(DEFAULT_IMAGE_URL);
            }
        }

        // Set the Product for the ProductDetail
        productDetail.setProduct(product);
        productDetail.getProduct().setProductId(product.getProductId());
        productDetail.setProduct(product);

        // Save the ProductDetail first
        productDetailRepository.save(productDetail);

        // Save the Product
        productRepository.save(product);

        System.out.println("Product and ProductDetail saved successfully.");
    }


    private String uploadImage(MultipartFile image) throws IOException {
        String uploadUrl = "https://api.imgbb.com/1/upload";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", IMGBB_API_KEY);
        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity,
                String.class);

        // Parse the JSON response to get the image URL
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject);
        return jsonObject.getJSONObject("data").getString("url");
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }
}
