package com.auca.library.service;

import com.auca.library.dao.MembershipDao;
import com.auca.library.domain.Membership;
import com.auca.library.domain.MembershipType;
import com.auca.library.domain.User;
import com.auca.library.exception.DuplicateMembershipException;
import com.auca.library.exception.MembershipNotFoundException;

import java.time.LocalDate;
import java.util.UUID;

public class MembershipService {

    private final MembershipDao membershipDao;
    private final UserService userService;

    public MembershipService(
            MembershipDao membershipDao,
            UserService userService
    ) {
        this.membershipDao = membershipDao;
        this.userService = userService;
    }

    public Membership registerMembership(
        UUID userId,
        UUID membershipTypeId
) {
    if (userId == null) {
        throw new IllegalArgumentException(
                "User ID is required."
        );
    }

    MembershipType membershipType =
            MembershipType.findById(membershipTypeId);

    LocalDate registrationDate = LocalDate.now();
    LocalDate expirationDate =
            registrationDate.plusYears(1);

    return registerMembership(
            userId,
            membershipType,
            registrationDate,
            expirationDate
    );
}

    public Membership registerMembership(
            UUID personId,
            MembershipType membershipType,
            LocalDate registrationDate,
            LocalDate expirationDate
    ) {
        validateRegistrationDetails(
                personId,
                membershipType,
                registrationDate,
                expirationDate
        );

        User libraryUser =
                userService.getUserById(personId);

        if (membershipDao
                .findByUserId(personId)
                .isPresent()) {
            throw new DuplicateMembershipException(
                    "The user already has a membership."
            );
        }

        Membership membership = new Membership(
                libraryUser,
                membershipType,
                registrationDate,
                expirationDate
        );

        return membershipDao.save(membership);
    }

    public Membership getMembershipById(
            UUID membershipId
    ) {
        if (membershipId == null) {
            throw new IllegalArgumentException(
                    "Membership ID is required."
            );
        }

        return membershipDao.findById(membershipId)
                .orElseThrow(() ->
                        new MembershipNotFoundException(
                                "Membership was not found."
                        )
                );
    }

    public Membership approveMembership(
        UUID membershipId
) {
    Membership membership =
            getMembershipById(membershipId);

    membership.approve();

    return membershipDao.update(membership);
}

public Membership rejectMembership(
        UUID membershipId
) {
    Membership membership =
            getMembershipById(membershipId);

    membership.reject();

    return membershipDao.update(membership);
}

    public Membership getMembershipByUserId(
            UUID personId
    ) {
        if (personId == null) {
            throw new IllegalArgumentException(
                    "Person ID is required."
            );
        }

        return membershipDao.findByUserId(personId)
                .orElseThrow(() ->
                        new MembershipNotFoundException(
                                "The user does not have a membership."
                        )
                );
    }

    private void validateRegistrationDetails(
            UUID personId,
            MembershipType membershipType,
            LocalDate registrationDate,
            LocalDate expirationDate
    ) {
        if (personId == null) {
            throw new IllegalArgumentException(
                    "Person ID is required."
            );
        }

        if (membershipType == null) {
            throw new IllegalArgumentException(
                    "Membership type is required."
            );
        }

        if (registrationDate == null) {
            throw new IllegalArgumentException(
                    "Registration date is required."
            );
        }

        if (expirationDate == null) {
            throw new IllegalArgumentException(
                    "Expiration date is required."
            );
        }

        if (expirationDate.isBefore(registrationDate)) {
            throw new IllegalArgumentException(
                    "Expiration date cannot be before "
                            + "the registration date."
            );
        }
    }
}