package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;

/**
 * Location value object (pickup or delivery point)
 *
 * @param address the street address
 * @param district the district
 * @param coordinates the geographic coordinates
 */
public record Location(String address, String district, Coordinates coordinates) {
    public Location {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");
        }
        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("District cannot be null or blank");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
    }
}
