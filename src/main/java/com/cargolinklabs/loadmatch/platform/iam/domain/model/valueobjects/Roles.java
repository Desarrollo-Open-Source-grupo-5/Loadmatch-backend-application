package com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects;

/**
 * Roles available for LoadMatch users
 */
public enum Roles {
    ROLE_SHIPPER,
    ROLE_CARRIER;

    /**
     * Converts a role name (e.g., "SHIPPER" or "ROLE_SHIPPER") into a role
     * @param name the role name
     * @return the matching role
     */
    public static Roles fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank");
        }
        var normalized = name.trim().toUpperCase();
        return Roles.valueOf(normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized);
    }
}
