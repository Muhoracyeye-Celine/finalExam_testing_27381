package com.auca.library.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MembershipTypeTest {

    @Test
    public void goldMembership_hasCorrectFeeAndBookLimit() {
        MembershipType goldMembership =
                MembershipType.GOLD;

        assertEquals(
                50,
                goldMembership
                        .getDailyLateFeeInRwandanFrancs()
        );

        assertEquals(
                5,
                goldMembership.getMaximumBorrowedBooks()
        );
    }

    @Test
    public void silverMembership_hasCorrectFeeAndBookLimit() {
        MembershipType silverMembership =
                MembershipType.SILVER;

        assertEquals(
                30,
                silverMembership
                        .getDailyLateFeeInRwandanFrancs()
        );

        assertEquals(
                3,
                silverMembership.getMaximumBorrowedBooks()
        );
    }

    @Test
    public void striverMembership_hasCorrectFeeAndBookLimit() {
        MembershipType striverMembership =
                MembershipType.STRIVER;

        assertEquals(
                10,
                striverMembership
                        .getDailyLateFeeInRwandanFrancs()
        );

        assertEquals(
                2,
                striverMembership.getMaximumBorrowedBooks()
        );
    }
}