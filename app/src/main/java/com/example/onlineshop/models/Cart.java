package com.example.onlineshop.models;

public class Cart {
    String productId;
    String productName;
    String productImageUrl;
    int numberOfItems;
    double price;

    public Cart() {
    }

    public Cart(String productId, String productName,String productImageUrl, int numberOfItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.numberOfItems = numberOfItems;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
