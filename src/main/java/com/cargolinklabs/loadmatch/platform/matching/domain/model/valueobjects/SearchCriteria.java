package com.cargolinklabs.loadmatch.platform.matching.domain.model.valueobjects;

import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;

/**
 * Search criteria used by a carrier to find nearby loads
 *
 * @param origin the current location of the carrier
 * @param radiusKm the maximum distance to the pickup point, in kilometers
 */
public record SearchCriteria(Coordinates origin, double radiusKm) {
    public static final double DEFAULT_RADIUS_KM = 30;
    private static final double MAX_RADIUS_KM = 500;

    public SearchCriteria {
        if (origin == null) {
            throw new IllegalArgumentException("Origin cannot be null");
        }
        if (radiusKm <= 0 || radiusKm > MAX_RADIUS_KM) {
            throw new IllegalArgumentException("Radius must be greater than 0 and up to %.0f km".formatted(MAX_RADIUS_KM));
        }
    }
}
