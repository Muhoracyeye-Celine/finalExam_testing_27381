package com.auca.library.domain;

public enum LocationType {
    PROVINCE(null),
    DISTRICT(PROVINCE),
    SECTOR(DISTRICT),
    CELL(SECTOR),
    VILLAGE(CELL);

    private final LocationType requiredParentType;

    LocationType(LocationType requiredParentType) {
        this.requiredParentType = requiredParentType;
    }

    public LocationType getRequiredParentType() {
        return requiredParentType;
    }

    public boolean requiresParent() {
        return requiredParentType != null;
    }

    public boolean acceptsParent(LocationType parentType) {
        return requiredParentType == parentType;
    }
}
