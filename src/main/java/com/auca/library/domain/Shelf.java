package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "shelves")
public class Shelf {

    @Id
    @Column(
            name = "shelf_id",
            nullable = false,
            updatable = false
    )
    private UUID shelfId;

    @Column(
            name = "shelf_code",
            nullable = false,
            unique = true,
            length = 30
    )
    private String shelfCode;

    @Column(
            name = "shelf_name",
            nullable = false,
            length = 100
    )
    private String shelfName;

    @Column(
            name = "book_category",
            length = 100
    )
    private String bookCategory;

    @Column(name = "initial_stock")
    private int initialStock;

    @Column(
            name = "available_stock",
            columnDefinition = "integer default 0"
    )
    private int availableStock;

    @Column(name = "borrowed_number")
    private int borrowedNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    protected Shelf() {
        // Required by Hibernate.
    }

    public Shelf(
            String shelfCode,
            String shelfName
    ) {
        this.shelfId = UUID.randomUUID();
        this.shelfCode = shelfCode;
        this.shelfName = shelfName;
        this.bookCategory = null;
        this.initialStock = 0;
        this.availableStock = 0;
        this.borrowedNumber = 0;
        this.room = null;
    }

    public UUID getShelfId() {
        return shelfId;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(
            String bookCategory
    ) {
        this.bookCategory = bookCategory;
    }

    public int getInitialStock() {
        return initialStock;
    }

    public void setInitialStock(int initialStock) {
        if (initialStock < 0) {
            throw new IllegalArgumentException(
                    "Initial stock cannot be negative."
            );
        }

        this.initialStock = initialStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(
            int availableStock
    ) {
        if (availableStock < 0) {
            throw new IllegalArgumentException(
                    "Available stock cannot be negative."
            );
        }

        this.availableStock = availableStock;
    }

    public int getBorrowedNumber() {
        return borrowedNumber;
    }

    public void setBorrowedNumber(
            int borrowedNumber
    ) {
        if (borrowedNumber < 0) {
            throw new IllegalArgumentException(
                    "Borrowed number cannot be negative."
            );
        }

        this.borrowedNumber = borrowedNumber;
    }

    public void incrementAvailableStock() {
        availableStock++;
    }

    public Room getRoom() {
        return room;
    }

    public void assignToRoom(Room room) {
        this.room = room;
    }
}