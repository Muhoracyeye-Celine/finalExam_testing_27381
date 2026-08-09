package com.auca.library.domain;

import java.util.UUID;

public enum MembershipType {

    GOLD(
            UUID.fromString(
                    "00000000-0000-0000-0000-000000000001"
            ),
            50,
            5
    ),

    SILVER(
            UUID.fromString(
                    "00000000-0000-0000-0000-000000000002"
            ),
            30,
            3
    ),

    STRIVER(
            UUID.fromString(
                    "00000000-0000-0000-0000-000000000003"
            ),
            10,
            2
    );

    private final UUID membershipTypeId;
    private final int dailyLateFeeInRwandanFrancs;
    private final int maximumBorrowedBooks;

    MembershipType(
            UUID membershipTypeId,
            int dailyLateFeeInRwandanFrancs,
            int maximumBorrowedBooks
    ) {
        this.membershipTypeId = membershipTypeId;
        this.dailyLateFeeInRwandanFrancs =
                dailyLateFeeInRwandanFrancs;
        this.maximumBorrowedBooks =
                maximumBorrowedBooks;
    }

    public UUID getMembershipTypeId() {
        return membershipTypeId;
    }

    public int getDailyLateFeeInRwandanFrancs() {
        return dailyLateFeeInRwandanFrancs;
    }

       

    public int getMaximumBorrowedBooks() {
        return maximumBorrowedBooks;
    }

    public static MembershipType findById(
            UUID membershipTypeId
    ) {
        if (membershipTypeId == null) {
            throw new IllegalArgumentException(
                    "Membership type ID is required."
            );
        }

        for (MembershipType membershipType : values()) {
            if (membershipType.membershipTypeId.equals(
                    membershipTypeId
            )) {
                return membershipType;
            }
        }

        throw new IllegalArgumentException(
                "Unknown membership type ID: "
                        + membershipTypeId
        );
    }
}