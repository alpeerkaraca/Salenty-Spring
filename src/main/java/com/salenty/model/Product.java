package com.salenty.model;

import jakarta.persistence.*;


@Entity(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private java.lang.Integer productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private double productPrice;

    @JoinColumn(name = "user_id", nullable = false)
    @Column(nullable = false)
    private int sellerId;

    @JoinColumn(name = "username", nullable = false)
    @Column
    private String sellerName;

    @Column
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 5000)
    private String productDescription;

    @Column
    private String productImage1;

    @Column
    private String productImage2;

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", category=" + category +
                ", productDescription='" + productDescription + '\'' +
                ", productImage1='" + productImage1 + '\'' +
                ", productImage2='" + productImage2 + '\'' +
                ", productImage3='" + productImage3 + '\'' +
                ", productSpecs='" + productSpecs + '\'' +
                '}';
    }

    @Column
    private String productImage3;

    @Column(nullable = false)
    private String productSpecs;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage1() {
        return productImage1;
    }

    public void setProductImage1(String productImage1) {
        this.productImage1 = productImage1;
    }

    public String getProductImage2() {
        return productImage2;
    }

    public void setProductImage2(String productImage2) {
        this.productImage2 = productImage2;
    }

    public String getProductImage3() {
        return productImage3;
    }

    public void setProductImage3(String productImage3) {
        this.productImage3 = productImage3;
    }

    public String getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(String productSpecs) {
        this.productSpecs = productSpecs;
    }
}
