package com.example.cinefast;

public class Snack {
    private int id;
    private String name;
    private double price;
    private int imageResId;
    private String imageName;
    private int quantity;

    // Constructor for hardcoded usage (backward compatible)
    public Snack(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 0;
    }

    // Constructor for SQLite usage
    public Snack(int id, String name, double price, String imageName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageName = imageName;
        this.quantity = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
