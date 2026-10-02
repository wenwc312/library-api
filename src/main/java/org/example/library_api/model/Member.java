package org.example.library_api.model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String name;
    private String userId;
    private List<Book> borrowedBooks;

    public Member() {
        borrowedBooks = new ArrayList<>();
    }

    public Member(String name, String userId) {
        this.name = name;
        this.userId = userId;
        borrowedBooks = new ArrayList<>();
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
