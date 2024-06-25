package com.salenty.model;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private String buyerFirstName;
    private String buyerLastName;
    private String buyerEmail;
    private String buyerAddress;
    private String buyerPhone;
    private String buyerCity;
    private String buyerState;
    private String buyerCountry;
    private String buyerZipCode;
    private String buyerApartment;
    private String orderDate;

    private String buyerCardNumber;
    private String buyerCardExpDate;
    private String buyerCardCvv;
    private String buyerCardName;

    private BigDecimal orderTotal;

    private OrderType orderType;

    @OneToMany
    @JoinColumn()
    private List<Product> product;

    private Status orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User buyer;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getBuyerFirstName() {
        return buyerFirstName;
    }

    public void setBuyerFirstName(String buyerFirstName) {
        this.buyerFirstName = buyerFirstName;
    }

    public String getBuyerLastName() {
        return buyerLastName;
    }

    public void setBuyerLastName(String buyerLastName) {
        this.buyerLastName = buyerLastName;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerCity() {
        return buyerCity;
    }

    public void setBuyerCity(String buyerCity) {
        this.buyerCity = buyerCity;
    }

    public String getBuyerState() {
        return buyerState;
    }

    public void setBuyerState(String buyerState) {
        this.buyerState = buyerState;
    }

    public String getBuyerCountry() {
        return buyerCountry;
    }

    public void setBuyerCountry(String buyerCountry) {
        this.buyerCountry = buyerCountry;
    }

    public String getBuyerZipCode() {
        return buyerZipCode;
    }

    public void setBuyerZipCode(String buyerZipCode) {
        this.buyerZipCode = buyerZipCode;
    }

    public String getBuyerApartment() {
        return buyerApartment;
    }

    public void setBuyerApartment(String buyerApartment) {
        this.buyerApartment = buyerApartment;
    }

    public String getBuyerCardNumber() {
        return buyerCardNumber;
    }

    public void setBuyerCardNumber(String buyerCardNumber) {
        this.buyerCardNumber = buyerCardNumber;
    }

    public String getBuyerCardExpDate() {
        return buyerCardExpDate;
    }

    public void setBuyerCardExpDate(String buyerCardExpDate) {
        this.buyerCardExpDate = buyerCardExpDate;
    }

    public String getBuyerCardCvv() {
        return buyerCardCvv;
    }

    public void setBuyerCardCvv(String buyerCardCvv) {
        this.buyerCardCvv = buyerCardCvv;
    }

    public String getBuyerCardName() {
        return buyerCardName;
    }

    public void setBuyerCardName(String buyerCardName) {
        this.buyerCardName = buyerCardName;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public List<Product> getProducts() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getBuyer() {
        return buyer;
    }

    @OnDelete(action = OnDeleteAction.CASCADE)
    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", buyerFirstName='" + buyerFirstName + '\'' +
                ", buyerLastName='" + buyerLastName + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", buyerPhone='" + buyerPhone + '\'' +
                ", buyerCity='" + buyerCity + '\'' +
                ", buyerState='" + buyerState + '\'' +
                ", buyerCountry='" + buyerCountry + '\'' +
                ", buyerZipCode='" + buyerZipCode + '\'' +
                ", buyerApartment='" + buyerApartment + '\'' +
                ", buyerCardNumber='" + buyerCardNumber + '\'' +
                ", buyerCardExpDate='" + buyerCardExpDate + '\'' +
                ", buyerCardCvv='" + buyerCardCvv + '\'' +
                ", buyerCardName='" + buyerCardName + '\'' +
                ", orderTotal=" + orderTotal +
                ", orderType=" + orderType +
                ", product=" + product +
                ", orderStatus=" + orderStatus +
                ", buyer=" + buyer +
                '}';
    }
}