package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

import java.time.LocalDate;

/**
 * Driver license value object
 *
 * @param number the license number
 * @param category the license category (e.g., A-IIb, A-IIIa)
 * @param expirationDate the expiration date
 */
public record DriverLicense(String number, String category, LocalDate expirationDate) {
    public DriverLicense {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("License number cannot be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("License category cannot be null or blank");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException("License expiration date cannot be null");
        }
        number = number.trim().toUpperCase();
        category = category.trim().toUpperCase();
    }

    /**
     * @return true when the license has not expired
     */
    public boolean isValid() {
        return !expirationDate.isBefore(LocalDate.now());
    }
}
