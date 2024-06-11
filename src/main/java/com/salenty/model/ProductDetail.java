package com.salenty.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_details") // Doğru tablo adı burada belirtiliyor
public class ProductDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int detailId;

  @Column(nullable = false)
  private String description;

  @Column
  private String image1;

  @Column
  private String image2;

  @Column
  private String image3;

  @Column(nullable = false)
  private String productSpecs;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  // Getters and Setters

  public int getDetailId() {
    return detailId;
  }

  public void setDetailId(int detailId) {
    this.detailId = detailId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage1() {
    return image1;
  }

  public void setImage1(String image1) {
    this.image1 = image1;
  }

  public String getImage2() {
    return image2;
  }

  public void setImage2(String image2) {
    this.image2 = image2;
  }

  public String getImage3() {
    return image3;
  }

  public void setImage3(String image3) {
    this.image3 = image3;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getProductSpecs() {
    return productSpecs;
  }

  public void setProductSpecs(String productSpecs) {
    this.productSpecs = productSpecs;
  }
}
