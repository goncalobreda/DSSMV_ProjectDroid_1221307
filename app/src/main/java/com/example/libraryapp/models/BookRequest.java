package com.example.libraryapp.models;

public class BookRequest {
    private int stock;

    public BookRequest(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
