package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(
            name = "isbn",
            nullable = false,
            length = 20
    )
    private String isbn;

    @Column(
            name = "title",
            nullable = false,
            length = 150
    )
    private String title;

    @Column(
            name = "author_name",
            nullable = false,
            length = 100
    )
    private String authorName;

    @Column(name = "edition")
    private Integer edition;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(
            name = "publisher_name",
            length = 100
    )
    private String publisherName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "book_status",
            nullable = false,
            length = 20
    )
    private BookStatus bookStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

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
        this.edition = null;
        this.publicationYear = null;
        this.publisherName = null;
        this.bookStatus = BookStatus.AVAILABLE;
        this.shelf = null;
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

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(
            Integer publicationYear
    ) {
        this.publicationYear = publicationYear;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(
            String publisherName
    ) {
        this.publisherName = publisherName;
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

    public void markAsReserved() {
        this.bookStatus = BookStatus.RESERVED;
    }

    public void markAsAvailable() {
        this.bookStatus = BookStatus.AVAILABLE;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void assignToShelf(Shelf shelf) {
        this.shelf = shelf;
    }
}