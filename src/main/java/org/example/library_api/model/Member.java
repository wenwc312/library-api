package org.example.library_api.model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String name;
    private String userId;
    private List<Book> borrowedBooks;

    public Member(String name, String userId) {
        this.name = name;
        this.userId = userId;
        borrowedBooks = new ArrayList<>();
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
}
