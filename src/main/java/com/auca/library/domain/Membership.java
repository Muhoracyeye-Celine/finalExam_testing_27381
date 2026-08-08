package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @Column(
            name = "membership_id",
            nullable = false,
            updatable = false
    )
    private UUID membershipId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User libraryUser;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "membership_type",
            nullable = false,
            length = 20
    )
    private MembershipType membershipType;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    protected Membership() {
        // Required by Hibernate.
    }

    public Membership(
            User libraryUser,
            MembershipType membershipType,
            LocalDate registrationDate,
            LocalDate expirationDate
    ) {
        this.membershipId = UUID.randomUUID();
        this.libraryUser = libraryUser;
        this.membershipType = membershipType;
        this.registrationDate = registrationDate;
        this.expirationDate = expirationDate;
    }

    public UUID getMembershipId() {
        return membershipId;
    }

    public User getLibraryUser() {
        return libraryUser;
    }

    public void setLibraryUser(User libraryUser) {
        this.libraryUser = libraryUser;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(
            MembershipType membershipType
    ) {
        this.membershipType = membershipType;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(
            LocalDate registrationDate
    ) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(
            LocalDate expirationDate
    ) {
        this.expirationDate = expirationDate;
    }

    public boolean isActiveOn(LocalDate dateToCheck) {
        if (dateToCheck == null) {
            throw new IllegalArgumentException(
                    "The date to check is required."
            );
        }

        boolean registrationHasStarted =
                !dateToCheck.isBefore(registrationDate);

        boolean membershipHasNotExpired =
                !dateToCheck.isAfter(expirationDate);

        return registrationHasStarted
                && membershipHasNotExpired;
    }
}