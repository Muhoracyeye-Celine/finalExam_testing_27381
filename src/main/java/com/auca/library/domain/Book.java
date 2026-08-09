package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(
            name = "book_id",
            nullable = false,
            updatable = false
    )
    private UUID bookId;

    @Column(
            name = "book_code",
            nullable = false,
            unique = true,
            length = 30
    )
    private String bookCode;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(
            name = "author_name",
            nullable = false,
            length = 100
    )
    private String authorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status", nullable = false, length = 20)
    private BookStatus bookStatus;

    protected Book() {
        // Required by Hibernate.
    }

    public Book(
            String bookCode,
            String isbn,
            String title,
            String authorName
    ) {
        this.bookId = UUID.randomUUID();
        this.bookCode = bookCode;
        this.isbn = isbn;
        this.title = title;
        this.authorName = authorName;
        this.bookStatus = BookStatus.AVAILABLE;
    }

    public UUID getBookId() {
        return bookId;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public boolean isAvailable() {
        return bookStatus == BookStatus.AVAILABLE;
    }

    public void markAsBorrowed() {
        this.bookStatus = BookStatus.BORROWED;
    }

    public void markAsAvailable() {
        this.bookStatus = BookStatus.AVAILABLE;
    }
}