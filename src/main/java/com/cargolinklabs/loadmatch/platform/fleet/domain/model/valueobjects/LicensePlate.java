package com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects;

/**
 * Peruvian license plate value object
 * <p>
 *     Accepts three letters or digits followed by three digits (e.g., ABC-123 or A1B-234) and normalizes
 *     the value to upper case with a hyphen.
 * </p>
 *
 * @param licensePlate the normalized license plate
 */
public record LicensePlate(String licensePlate) {
    public LicensePlate {
        if (licensePlate == null) {
            throw new IllegalArgumentException("License plate cannot be null");
        }
        var normalized = licensePlate.trim().toUpperCase().replace(" ", "");
        if (!normalized.matches("^[A-Z0-9]{3}-?\\d{3}$")) {
            throw new IllegalArgumentException("License plate must have the format ABC-123");
        }
        licensePlate = normalized.contains("-") ? normalized : normalized.substring(0, 3) + "-" + normalized.substring(3);
    }
}
