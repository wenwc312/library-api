package org.example.library_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Id
    private String ISBN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_by")
    @JsonIgnore
    private Member borrowedBy;

    public Book() {
    }

    public Book(String name, String author, String ISBN) {
        this.name = name;
        this.author = author;
        this.ISBN = ISBN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public boolean isBorrowed() {
        return borrowedBy!=null;
    }

    public Member getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(Member borrowedBy) {
        this.borrowedBy = borrowedBy;
    }
}
