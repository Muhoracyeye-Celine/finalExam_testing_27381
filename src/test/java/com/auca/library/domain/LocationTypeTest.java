package com.auca.library.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocationTypeTest {

    @Test
    public void provinceDoesNotRequireParent() {
        assertFalse(LocationType.PROVINCE.requiresParent());
    }

    @Test
    public void districtAcceptsProvinceAsParent() {
        assertTrue(LocationType.DISTRICT.acceptsParent(LocationType.PROVINCE));
    }

    @Test
    public void districtRejectsSectorAsParent() {
        assertFalse(LocationType.DISTRICT.acceptsParent(LocationType.SECTOR));
    }
}
