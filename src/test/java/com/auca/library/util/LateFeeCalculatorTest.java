package com.auca.library.util;

import com.auca.library.domain.MembershipType;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class LateFeeCalculatorTest {

    private static final LocalDate DUE_DATE =
            LocalDate.of(2026, 8, 1);

    private static final LocalDate THREE_DAYS_LATE =
            LocalDate.of(2026, 8, 4);

    @Test
    public void goldMembership_threeLateDays_costs150Rwf() {
        int lateFee = LateFeeCalculator.calculateLateFee(
                MembershipType.GOLD,
                DUE_DATE,
                THREE_DAYS_LATE
        );

        assertEquals(150, lateFee);
    }

    @Test
    public void silverMembership_threeLateDays_costs90Rwf() {
        int lateFee = LateFeeCalculator.calculateLateFee(
                MembershipType.SILVER,
                DUE_DATE,
                THREE_DAYS_LATE
        );

        assertEquals(90, lateFee);
    }

    @Test
    public void striverMembership_threeLateDays_costs30Rwf() {
        int lateFee = LateFeeCalculator.calculateLateFee(
                MembershipType.STRIVER,
                DUE_DATE,
                THREE_DAYS_LATE
        );

        assertEquals(30, lateFee);
    }

    @Test
    public void bookReturnedOnDueDate_hasNoLateFee() {
        int lateFee = LateFeeCalculator.calculateLateFee(
                MembershipType.GOLD,
                DUE_DATE,
                DUE_DATE
        );

        assertEquals(0, lateFee);
    }
}