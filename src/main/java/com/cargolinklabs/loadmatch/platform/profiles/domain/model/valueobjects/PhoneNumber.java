package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

/**
 * Peruvian mobile phone number value object
 *
 * @param phoneNumber the 9-digit phone number
 */
public record PhoneNumber(String phoneNumber) {
    public PhoneNumber {
        if (phoneNumber == null || !phoneNumber.trim().matches("\\d{9}")) {
            throw new IllegalArgumentException("Phone number must have 9 digits");
        }
        phoneNumber = phoneNumber.trim();
    }
}
