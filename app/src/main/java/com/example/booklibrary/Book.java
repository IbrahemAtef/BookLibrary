package com.example.booklibrary;

import android.net.Uri;

public class Book {

    String name, auther, price, discription;
    Uri bookUrl;

    public Book() {
    }

    public Book(String name, String auther, String price, String discription, Uri bookUrl) {
        this.name = name;
        this.auther = auther;
        this.price = price;
        this.discription = discription;
        this.bookUrl = bookUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Uri getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(Uri bookUrl) {
        this.bookUrl = bookUrl;
    }
}
