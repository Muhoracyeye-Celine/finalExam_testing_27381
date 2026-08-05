package com.auca.library.service;

import com.auca.library.dao.LocationDao;
import com.auca.library.domain.Location;
import com.auca.library.domain.LocationType;
import com.auca.library.exception.DuplicateLocationCodeException;
import com.auca.library.exception.InvalidLocationHierarchyException;
import com.auca.library.exception.LocationNotFoundException;

import java.util.UUID;

public class LocationService {

    private final LocationDao locationDao;

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public Location createLocation(
            Location location,
            UUID parentId
    ) {
        validateLocationDetails(location);

        String normalizedCode =
                location.getLocationCode().trim().toUpperCase();

        location.setLocationCode(normalizedCode);
        location.setLocationName(
                location.getLocationName().trim()
        );

        if (locationDao.findByCode(normalizedCode).isPresent()) {
            throw new DuplicateLocationCodeException(
                    "Location code already exists: "
                            + normalizedCode
            );
        }

        LocationType locationType =
                location.getLocationType();

        if (!locationType.requiresParent()) {
            if (parentId != null) {
                throw new InvalidLocationHierarchyException(
                        "A province cannot have a parent location."
                );
            }

            location.setParent(null);
        } else {
            assignParentLocation(
                    location,
                    locationType,
                    parentId
            );
        }

        return locationDao.save(location);
    }

    public String getProvinceNameByVillageId(UUID villageId) {
        if (villageId == null) {
            throw new IllegalArgumentException(
                    "Village ID is required."
            );
        }

        Location village = locationDao.findById(villageId)
                .orElseThrow(() ->
                        new LocationNotFoundException(
                                "Village was not found."
                        )
                );

        if (village.getLocationType()
                != LocationType.VILLAGE) {
            throw new InvalidLocationHierarchyException(
                    "The provided ID does not belong to a village."
            );
        }

        return findProvinceName(village);
    }

    private void assignParentLocation(
            Location location,
            LocationType locationType,
            UUID parentId
    ) {
        if (parentId == null) {
            throw new InvalidLocationHierarchyException(
                    locationType
                            + " requires a parent location."
            );
        }

        Location parentLocation =
                locationDao.findById(parentId)
                        .orElseThrow(() ->
                                new LocationNotFoundException(
                                        "Parent location was not found."
                                )
                        );

        if (!locationType.acceptsParent(
                parentLocation.getLocationType()
        )) {
            throw new InvalidLocationHierarchyException(
                    locationType
                            + " cannot have "
                            + parentLocation.getLocationType()
                            + " as its parent."
            );
        }

        location.setParent(parentLocation);
    }

    private String findProvinceName(
            Location startingLocation
    ) {
        Location currentLocation = startingLocation;

        while (currentLocation.getLocationType()
                != LocationType.PROVINCE) {

            Location parentLocation =
                    currentLocation.getParent();

            if (parentLocation == null) {
                throw new InvalidLocationHierarchyException(
                        "The location hierarchy is incomplete."
                );
            }

            UUID parentId =
                    parentLocation.getLocationId();

            currentLocation =
                    locationDao.findById(parentId)
                            .orElseThrow(() ->
                                    new LocationNotFoundException(
                                            "A parent location was not found."
                                    )
                            );
        }

        return currentLocation.getLocationName();
    }

    private void validateLocationDetails(Location location) {
        if (location == null) {
            throw new IllegalArgumentException(
                    "Location is required."
            );
        }

        if (location.getLocationCode() == null
                || location.getLocationCode().isBlank()) {
            throw new IllegalArgumentException(
                    "Location code is required."
            );
        }

        if (location.getLocationName() == null
                || location.getLocationName().isBlank()) {
            throw new IllegalArgumentException(
                    "Location name is required."
            );
        }

        if (location.getLocationType() == null) {
            throw new IllegalArgumentException(
                    "Location type is required."
            );
        }
    }
}