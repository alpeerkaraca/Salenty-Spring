package com.salenty.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.salenty.model.Product;
import com.salenty.repositories.CategoryRepository;
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
import java.util.ArrayList;
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
        // Save the product details in the database
        product.setSellerId(1);
        product.setSellerName(userService.getUserById(product.getSellerId()).getUserName());
        product.getCategory().setCategoryName(categoryService.getCategoryById(product.getCategory().getCategoryId()));
        product.setProductId(productRepository.findAll().size() + 1);

        System.out.println(
                "İSTEK ALINDI \n" +
                        "Product Specs \n " +
                        "Product Name: " + product.getProductName() +
                        "\n Product ID: " + product.getProductId() +
                        "\n Product Price: " + product.getProductPrice() +
                        "\n Seller ID: " + product.getSellerId() +
                        "\n Seller Name: " + product.getSellerName() +
                        "\n Product Image: " + product.getProductImage() +
                        "\n Categories" +
                        "\n CategoryID: " + product.getCategory().getCategoryId() +
                        "\n CategoryName: " + product.getCategory().getCategoryName() +
                        "\n Product Detail:" +
                        "\n DetailID: " + product.getProductDetail().getDetailId() +
                        "\n Product Detail: " + product.getProductDetail().getDescription() +
                        "\n Product Specs: " + product.getProductDetail().getProductSpecs() +
                        "\n Image 1: " + product.getProductDetail().getImage1() +
                        "\n Image 2: " + product.getProductDetail().getImage2() +
                        "\n Image 3: " + product.getProductDetail().getImage3());

        // productRepository.save(product);
        System.out.println("Cover Image Yükleniyor...");
        // Upload the cover image to imgbb.com and set the URL
        if (!coverImage.isEmpty()) {
            String coverImageUrl = uploadImage(coverImage);
            System.out.println("Cover Image Yüklendi: " + coverImageUrl);
            product.setProductImage(coverImageUrl);
        }

        // Upload other images and set their URLs
        if (images != null && images.length > 0) {
            for (int i = 0; i < images.length && i < 3; i++) {
                if (!images[i].isEmpty()) {
                    String imageUrl = uploadImage(images[i]);
                    if (i == 0) {
                        product.getProductDetail().setImage1(imageUrl);
                    } else if (i == 1) {
                        product.getProductDetail().setImage2(imageUrl);
                    } else if (i == 2) {
                        product.getProductDetail().setImage3(imageUrl);
                    }
                } else {
                    if (i == 0) {
                        product.getProductDetail().setImage1(DEFAULT_IMAGE_URL);
                    } else if (i == 1) {
                        product.getProductDetail().setImage2(DEFAULT_IMAGE_URL);
                    } else if (i == 2) {
                        product.getProductDetail().setImage3(DEFAULT_IMAGE_URL);
                    }
                }
            }
        }

        // Ensure all image fields are set to default if not provided
        if (images == null || images.length < 3) {
            if (images == null || images.length == 0 || images[0].isEmpty()) {
                product.getProductDetail().setImage1(DEFAULT_IMAGE_URL);
            }
            if (images == null || images.length < 2 || images[1].isEmpty()) {
                product.getProductDetail().setImage2(DEFAULT_IMAGE_URL);
            }
            if (images == null || images.length < 3 || images[2].isEmpty()) {
                product.getProductDetail().setImage3(DEFAULT_IMAGE_URL);
            }
        }

        System.out.println(
                "KAYIT TEST \n" +
                        "Product Specs \n " +
                        "Product Name: " + product.getProductName() +
                        "\n Product ID: " + product.getProductId() +
                        "\n Product Price: " + product.getProductPrice() +
                        "\n Seller ID: " + product.getSellerId() +
                        "\n Seller Name: " + product.getSellerName() +
                        "\n Product Image: " + product.getProductImage() +
                        "\n Categories" +
                        "\n CategoryID: " + product.getCategory().getCategoryId() +
                        "\n CategoryName: " + product.getCategory().getCategoryName() +
                        "\n Product Detail:" +
                        "\n DetailID: " + product.getProductDetail().getDetailId() +
                        "\n Product Detail: " + product.getProductDetail().getDescription() +
                        "\n Product Specs: " + product.getProductDetail().getProductSpecs() +
                        "\n Image 1: " + product.getProductDetail().getImage1() +
                        "\n Image 2: " + product.getProductDetail().getImage2() +
                        "\n Image 3: " + product.getProductDetail().getImage3());

        productDetailRepository.save(product.getProductDetail());
        productRepository.save(product);
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

}
