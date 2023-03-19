package com.example.epub.ReadBook;

import java.io.Serializable;

public class BookModel implements Serializable {
    private String bookCover;
    private String bookTitle;
    private String bookAuthor;
    private String bookLanguage;
    private String bookGenre;
    private String bookURL;

    public String getBookURL() {
        return bookURL;
    }

    public void setBookURL(String bookURL) {
        this.bookURL = bookURL;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

}
