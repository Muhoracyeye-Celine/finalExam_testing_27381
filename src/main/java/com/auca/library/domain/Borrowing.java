package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "borrowings")
public class Borrowing {

    @Id
    @Column(
            name = "borrowing_id",
            nullable = false,
            updatable = false
    )
    private UUID borrowingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "membership_id",
            nullable = false
    )
    private Membership membership;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrowed_date", nullable = false)
    private LocalDate borrowedDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(
            name = "late_fee_in_rwandan_francs",
            nullable = false
    )
    private int lateFeeInRwandanFrancs;

    protected Borrowing() {
        // Required by Hibernate.
    }

    public Borrowing(
            Membership membership,
            Book book,
            LocalDate borrowedDate,
            LocalDate dueDate
    ) {
        this.borrowingId = UUID.randomUUID();
        this.membership = membership;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.lateFeeInRwandanFrancs = 0;
    }

    public UUID getBorrowingId() {
        return borrowingId;
    }

    public Membership getMembership() {
        return membership;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public int getLateFeeInRwandanFrancs() {
        return lateFeeInRwandanFrancs;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public void markAsReturned(LocalDate returnDate) {
        if (returnDate == null) {
            throw new IllegalArgumentException(
                    "Return date is required."
            );
        }

        this.returnDate = returnDate;
    }

    public void setLateFeeInRwandanFrancs(
            int lateFeeInRwandanFrancs
    ) {
        if (lateFeeInRwandanFrancs < 0) {
            throw new IllegalArgumentException(
                    "Late fee cannot be negative."
            );
        }

        this.lateFeeInRwandanFrancs =
                lateFeeInRwandanFrancs;
    }
}