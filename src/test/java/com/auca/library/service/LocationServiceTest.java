package com.auca.library.service;

import com.auca.library.dao.LocationDao;
import com.auca.library.domain.Location;
import com.auca.library.domain.LocationType;
import com.auca.library.exception.DuplicateLocationCodeException;
import com.auca.library.exception.InvalidLocationHierarchyException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LocationServiceTest {

    private final LocationService locationService =
            new LocationService(new LocationDao());

    @Test
    public void createProvince_withNoParent_succeeds() {
        Location province = new Location(
                uniqueCode("PR"),
                "Kigali",
                LocationType.PROVINCE
        );

        Location savedProvince =
                locationService.createLocation(
                        province,
                        null
                );

        assertNotNull(savedProvince.getLocationId());
        assertNull(savedProvince.getParent());
        assertEquals(
                LocationType.PROVINCE,
                savedProvince.getLocationType()
        );
    }

    @Test
    public void createDistrict_withValidProvinceParent_succeeds() {
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

        assertNotNull(district.getLocationId());
        assertNotNull(district.getParent());

        assertEquals(
                province.getLocationId(),
                district.getParent().getLocationId()
        );
    }

    @Test(
            expected =
                    InvalidLocationHierarchyException.class
    )
    public void createDistrict_withMissingParent_throwsException() {
        Location district = new Location(
                uniqueCode("DI"),
                "Gasabo",
                LocationType.DISTRICT
        );

        locationService.createLocation(
                district,
                null
        );
    }

    @Test(
            expected =
                    DuplicateLocationCodeException.class
    )
    public void createLocation_duplicateLocationCode_throwsException() {
        String duplicateCode = uniqueCode("PR");

        Location firstProvince = new Location(
                duplicateCode,
                "Kigali",
                LocationType.PROVINCE
        );

        locationService.createLocation(
                firstProvince,
                null
        );

        Location secondProvince = new Location(
                duplicateCode.toLowerCase(),
                "Northern Province",
                LocationType.PROVINCE
        );

        locationService.createLocation(
                secondProvince,
                null
        );
    }

    @Test
    public void validVillageId_returnsCorrectProvinceName() {
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

        Location village = locationService.createLocation(
                new Location(
                        uniqueCode("VI"),
                        "Urugwiro",
                        LocationType.VILLAGE
                ),
                cell.getLocationId()
        );

        String provinceName =
                locationService.getProvinceNameByVillageId(
                        village.getLocationId()
                );

        assertEquals(
                "Kigali",
                provinceName
        );
    }

    private String uniqueCode(String prefix) {
        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        return prefix + "-" + randomPart;
    }
}