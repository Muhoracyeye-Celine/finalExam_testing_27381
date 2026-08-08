package com.auca.library.service;

import com.auca.library.dao.UserDao;
import com.auca.library.domain.Gender;
import com.auca.library.domain.Location;
import com.auca.library.domain.LocationType;
import com.auca.library.domain.Role;
import com.auca.library.domain.User;
import com.auca.library.exception.DuplicateUsernameException;
import com.auca.library.exception.InvalidLocationHierarchyException;
import com.auca.library.exception.UserNotFoundException;
import com.auca.library.util.PasswordUtil;

import java.util.UUID;

public class UserService {

    private final UserDao userDao;
    private final LocationService locationService;

    public UserService(
            UserDao userDao,
            LocationService locationService
    ) {
        this.userDao = userDao;
        this.locationService = locationService;
    }

    public User registerUser(
            String firstName,
            String lastName,
            Gender gender,
            String phoneNumber,
            String username,
            String rawPassword,
            Role role,
            UUID villageId
    ) {
        validateRegistrationDetails(
                firstName,
                lastName,
                gender,
                phoneNumber,
                username,
                role,
                villageId
        );

        String normalizedUsername =
                username.trim().toLowerCase();

        if (userDao.findByUsername(
                normalizedUsername
        ).isPresent()) {
            throw new DuplicateUsernameException(
                    "Username already exists: "
                            + normalizedUsername
            );
        }

        Location village =
                locationService.getLocationById(villageId);

        if (village.getLocationType()
                != LocationType.VILLAGE) {
            throw new InvalidLocationHierarchyException(
                    "A user must be assigned to a village."
            );
        }

        String passwordHash =
                PasswordUtil.hashPassword(rawPassword);

        User user = new User(
                firstName.trim(),
                lastName.trim(),
                gender,
                phoneNumber.trim(),
                normalizedUsername,
                passwordHash,
                role,
                village
        );

        return userDao.save(user);
    }

    public String getProvinceNameByPersonId(
            UUID personId
    ) {
        if (personId == null) {
            throw new IllegalArgumentException(
                    "Person ID is required."
            );
        }

        User user = userDao.findById(personId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User was not found."
                        )
                );

        UUID villageId =
                user.getVillage().getLocationId();

        return locationService
                .getProvinceNameByVillageId(villageId);
    }

    public boolean authenticate(
            String username,
            String rawPassword
    ) {
        if (username == null
                || username.isBlank()
                || rawPassword == null
                || rawPassword.isBlank()) {
            return false;
        }

        return userDao.findByUsername(username.trim())
                .map(user ->
                        PasswordUtil.matches(
                                rawPassword,
                                user.getPasswordHash()
                        )
                )
                .orElse(false);
    }

    public User getUserById(UUID personId) {
    if (personId == null) {
        throw new IllegalArgumentException(
                "Person ID is required."
        );
    }

    return userDao.findById(personId)
            .orElseThrow(() ->
                    new UserNotFoundException(
                            "User was not found."
                    )
            );
}

    private void validateRegistrationDetails(
            String firstName,
            String lastName,
            Gender gender,
            String phoneNumber,
            String username,
            Role role,
            UUID villageId
    ) {
        requireText(firstName, "First name");
        requireText(lastName, "Last name");
        requireText(phoneNumber, "Phone number");
        requireText(username, "Username");

        if (gender == null) {
            throw new IllegalArgumentException(
                    "Gender is required."
            );
        }

        if (role == null) {
            throw new IllegalArgumentException(
                    "Role is required."
            );
        }

        if (role == Role.LIBRARIAN) {
            throw new IllegalArgumentException(
                    "A librarian account cannot self-register."
            );
        }

        if (villageId == null) {
            throw new IllegalArgumentException(
                    "Village ID is required."
            );
        }
    }

    private void requireText(
            String value,
            String fieldName
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }
    }
}