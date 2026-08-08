package com.auca.library.domain;

public enum MembershipType {

    GOLD(50, 5),
    SILVER(30, 3),
    STRIVER(10, 2);

    private final int dailyLateFeeInRwandanFrancs;
    private final int maximumBorrowedBooks;

    MembershipType(
            int dailyLateFeeInRwandanFrancs,
            int maximumBorrowedBooks
    ) {
        this.dailyLateFeeInRwandanFrancs =
                dailyLateFeeInRwandanFrancs;
        this.maximumBorrowedBooks =
                maximumBorrowedBooks;
    }

    public int getDailyLateFeeInRwandanFrancs() {
        return dailyLateFeeInRwandanFrancs;
    }

    public int getMaximumBorrowedBooks() {
        return maximumBorrowedBooks;
    }
}