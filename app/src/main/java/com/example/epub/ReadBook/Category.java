package com.example.epub.ReadBook;

public class Category {
    int image;
    String title;
    String genre;

    public Category(int image, String title, String genre) {
        this.title = title;
        this.image = image;
        this.genre = genre;
    }

    public Category(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
