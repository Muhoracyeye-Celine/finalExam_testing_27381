package com.auca.library.util;

import com.auca.library.domain.MembershipType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class LateFeeCalculator {

    private LateFeeCalculator() {
        // Prevents objects of this utility class.
    }

    public static int calculateLateFee(
            MembershipType membershipType,
            LocalDate dueDate,
            LocalDate returnDate
    ) {
        if (membershipType == null) {
            throw new IllegalArgumentException(
                    "Membership type is required."
            );
        }

        if (dueDate == null) {
            throw new IllegalArgumentException(
                    "Due date is required."
            );
        }

        if (returnDate == null) {
            throw new IllegalArgumentException(
                    "Return date is required."
            );
        }

        long numberOfLateDays =
                ChronoUnit.DAYS.between(
                        dueDate,
                        returnDate
                );

        if (numberOfLateDays <= 0) {
            return 0;
        }

        int dailyLateFee =
                membershipType
                        .getDailyLateFeeInRwandanFrancs();

        long totalLateFee =
                numberOfLateDays * dailyLateFee;

        return Math.toIntExact(totalLateFee);
    }
}