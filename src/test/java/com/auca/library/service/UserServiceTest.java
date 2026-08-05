package com.auca.library.service;

import com.auca.library.dao.LocationDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Gender;
import com.auca.library.domain.Location;
import com.auca.library.domain.LocationType;
import com.auca.library.domain.Role;
import com.auca.library.domain.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserServiceTest {

    private static final LocationService locationService =
            new LocationService(new LocationDao());

    private static final UserService userService =
            new UserService(
                    new UserDao(),
                    locationService
            );

    private static Location testVillage;

    @BeforeClass
    public static void createTestLocationHierarchy() {
        Location province = locationService.createLocation(
                new Location(
                        uniqueCode("PR"),
                        "Kigali",
                        LocationType.PROVINCE
                ),
                null
        );

        Location district = locationService.createLocation(
                new Location(
                        uniqueCode("DI"),
                        "Gasabo",
                        LocationType.DISTRICT
                ),
                province.getLocationId()
        );

        Location sector = locationService.createLocation(
                new Location(
                        uniqueCode("SE"),
                        "Kacyiru",
                        LocationType.SECTOR
                ),
                district.getLocationId()
        );

        Location cell = locationService.createLocation(
                new Location(
                        uniqueCode("CE"),
                        "Kamatamu",
                        LocationType.CELL
                ),
                sector.getLocationId()
        );

        testVillage = locationService.createLocation(
                new Location(
                        uniqueCode("VI"),
                        "Urugwiro",
                        LocationType.VILLAGE
                ),
                cell.getLocationId()
        );
    }

    @Test
    public void validPersonId_returnsCorrectProvinceName() {
        User user = registerStudent("Library@123");

        String provinceName =
                userService.getProvinceNameByPersonId(
                        user.getPersonId()
                );

        assertEquals("Kigali", provinceName);
    }

    @Test
    public void authenticate_correctCredentials_returnsTrue() {
        String rawPassword = "Library@123";

        User user = registerStudent(rawPassword);

        boolean authenticated =
                userService.authenticate(
                        user.getUsername(),
                        rawPassword
                );

        assertTrue(authenticated);
    }

    @Test
    public void authenticate_wrongPassword_returnsFalse() {
        User user = registerStudent("Library@123");

        boolean authenticated =
                userService.authenticate(
                        user.getUsername(),
                        "WrongPassword"
                );

        assertFalse(authenticated);
    }

    @Test
    public void authenticate_unknownUsername_returnsFalse() {
        boolean authenticated =
                userService.authenticate(
                        uniqueUsername(),
                        "Library@123"
                );

        assertFalse(authenticated);
    }

    @Test
    public void authenticate_nullOrBlankInput_returnsFalse() {
        assertFalse(
                userService.authenticate(
                        null,
                        "Library@123"
                )
        );

        assertFalse(
                userService.authenticate(
                        "   ",
                        "Library@123"
                )
        );

        assertFalse(
                userService.authenticate(
                        uniqueUsername(),
                        null
                )
        );

        assertFalse(
                userService.authenticate(
                        uniqueUsername(),
                        "   "
                )
        );
    }

    private User registerStudent(String rawPassword) {
        return userService.registerUser(
                "Celine",
                "Muhoracyeye",
                Gender.FEMALE,
                "0788000000",
                uniqueUsername(),
                rawPassword,
                Role.STUDENT,
                testVillage.getLocationId()
        );
    }

    private static String uniqueCode(String prefix) {
        return prefix
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }

    private static String uniqueUsername() {
        return "user_"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }
}