package com.auca.library.service;

import com.auca.library.dao.LocationDao;
import com.auca.library.dao.MembershipDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Gender;
import com.auca.library.domain.Location;
import com.auca.library.domain.LocationType;
import com.auca.library.domain.Membership;
import com.auca.library.domain.MembershipType;
import com.auca.library.domain.Role;
import com.auca.library.domain.User;
import com.auca.library.exception.DuplicateMembershipException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MembershipServiceTest {

    private static MembershipService membershipService;
    private static UserService userService;
    private static Location testVillage;

    @BeforeClass
    public static void prepareServicesAndLocationHierarchy() {
        LocationService locationService =
                new LocationService(new LocationDao());

        userService = new UserService(
                new UserDao(),
                locationService
        );

        membershipService = new MembershipService(
                new MembershipDao(),
                userService
        );

        Location province = locationService.createLocation(
                new Location(
                        uniqueCode("MP"),
                        "Membership Test Province",
                        LocationType.PROVINCE
                ),
                null
        );

        Location district = locationService.createLocation(
                new Location(
                        uniqueCode("MD"),
                        "Membership Test District",
                        LocationType.DISTRICT
                ),
                province.getLocationId()
        );

        Location sector = locationService.createLocation(
                new Location(
                        uniqueCode("MS"),
                        "Membership Test Sector",
                        LocationType.SECTOR
                ),
                district.getLocationId()
        );

        Location cell = locationService.createLocation(
                new Location(
                        uniqueCode("MC"),
                        "Membership Test Cell",
                        LocationType.CELL
                ),
                sector.getLocationId()
        );

        testVillage = locationService.createLocation(
                new Location(
                        uniqueCode("MV"),
                        "Membership Test Village",
                        LocationType.VILLAGE
                ),
                cell.getLocationId()
        );
    }

    @Test
    public void registerMembership_withValidDetails_succeeds() {
        User libraryUser = registerStudent();

        LocalDate registrationDate = LocalDate.now();
        LocalDate expirationDate =
                registrationDate.plusYears(1);

        Membership membership =
                membershipService.registerMembership(
                        libraryUser.getPersonId(),
                        MembershipType.GOLD,
                        registrationDate,
                        expirationDate
                );

        assertNotNull(membership.getMembershipId());

        assertEquals(
                libraryUser.getPersonId(),
                membership.getLibraryUser().getPersonId()
        );

        assertEquals(
                MembershipType.GOLD,
                membership.getMembershipType()
        );

        assertEquals(
                registrationDate,
                membership.getRegistrationDate()
        );

        assertEquals(
                expirationDate,
                membership.getExpirationDate()
        );
    }

    @Test(expected = DuplicateMembershipException.class)
    public void registerMembership_forExistingMember_throwsException() {
        User libraryUser = registerStudent();

        LocalDate registrationDate = LocalDate.now();
        LocalDate expirationDate =
                registrationDate.plusYears(1);

        membershipService.registerMembership(
                libraryUser.getPersonId(),
                MembershipType.SILVER,
                registrationDate,
                expirationDate
        );

        membershipService.registerMembership(
                libraryUser.getPersonId(),
                MembershipType.GOLD,
                registrationDate,
                expirationDate
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerMembership_withInvalidDates_throwsException() {
        User libraryUser = registerStudent();

        LocalDate registrationDate = LocalDate.now();
        LocalDate invalidExpirationDate =
                registrationDate.minusDays(1);

        membershipService.registerMembership(
                libraryUser.getPersonId(),
                MembershipType.STRIVER,
                registrationDate,
                invalidExpirationDate
        );
    }

    @Test
    public void getMembershipByUserId_returnsRegisteredMembership() {
        User libraryUser = registerStudent();

        Membership registeredMembership =
                membershipService.registerMembership(
                        libraryUser.getPersonId(),
                        MembershipType.SILVER,
                        LocalDate.now(),
                        LocalDate.now().plusYears(1)
                );

        Membership foundMembership =
                membershipService.getMembershipByUserId(
                        libraryUser.getPersonId()
                );

        assertEquals(
                registeredMembership.getMembershipId(),
                foundMembership.getMembershipId()
        );

        assertEquals(
                MembershipType.SILVER,
                foundMembership.getMembershipType()
        );
    }

    private static User registerStudent() {
        return userService.registerUser(
                "celine",
                "Muhoracyeye",
                Gender.FEMALE,
                "0788000000",
                uniqueUsername(),
                "Password123!",
                Role.STUDENT,
                testVillage.getLocationId()
        );
    }

    private static String uniqueCode(String prefix) {
        return prefix
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private static String uniqueUsername() {
        return "member_"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }
}