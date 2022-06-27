package com.example.onlineshop.models;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String id;
    private String name;
    private double price;
    private String image;
    private float rating;

    public Product(String id, String name, double price, String image, float rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.rating = rating;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id",id);
        result.put("name",name);
        result.put("price",price);
        result.put("image",image);
        result.put("rating",rating);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
