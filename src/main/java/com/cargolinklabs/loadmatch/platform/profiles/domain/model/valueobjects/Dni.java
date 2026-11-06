package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

/**
 * Peruvian national identity document (DNI) value object
 *
 * @param dni the 8-digit DNI
 */
public record Dni(String dni) {
    public Dni {
        if (dni == null || !dni.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI must have 8 digits");
        }
        dni = dni.trim();
    }
}
