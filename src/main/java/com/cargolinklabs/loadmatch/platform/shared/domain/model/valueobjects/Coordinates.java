package com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects;

/**
 * Geographic coordinates value object shared by the bounded contexts
 *
 * @param latitude the latitude in decimal degrees (-90 to 90)
 * @param longitude the longitude in decimal degrees (-180 to 180)
 */
public record Coordinates(double latitude, double longitude) {
    private static final double EARTH_RADIUS_KM = 6371.0;

    public Coordinates {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }

    /**
     * Calculates the great-circle distance to other coordinates with the Haversine formula
     * <p>
     *     a = sin²(Δφ/2) + cos φ1 · cos φ2 · sin²(Δλ/2)<br>
     *     d = 2 · R · atan2(√a, √(1−a))
     * </p>
     *
     * @param other the other coordinates
     * @return the distance in kilometers
     */
    public double distanceTo(Coordinates other) {
        var latitude1 = Math.toRadians(this.latitude);
        var latitude2 = Math.toRadians(other.latitude);
        var deltaLatitude = Math.toRadians(other.latitude - this.latitude);
        var deltaLongitude = Math.toRadians(other.longitude - this.longitude);
        var a = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(latitude1) * Math.cos(latitude2) * Math.pow(Math.sin(deltaLongitude / 2), 2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
