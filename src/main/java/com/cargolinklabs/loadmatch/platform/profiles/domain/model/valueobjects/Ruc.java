package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

/**
 * Peruvian taxpayer identification number (RUC) value object
 * <p>
 *     A RUC has 11 digits and its last digit is a check digit computed with the modulo 11 algorithm.
 * </p>
 *
 * @param ruc the 11-digit RUC
 */
public record Ruc(String ruc) {
    private static final int[] WEIGHTS = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    public Ruc {
        if (ruc == null || !ruc.trim().matches("\\d{11}")) {
            throw new IllegalArgumentException("RUC must have 11 digits");
        }
        ruc = ruc.trim();
        if (computeCheckDigit(ruc) != Character.getNumericValue(ruc.charAt(10))) {
            throw new IllegalArgumentException("RUC check digit is not valid");
        }
    }

    private static int computeCheckDigit(String ruc) {
        var sum = 0;
        for (var i = 0; i < WEIGHTS.length; i++) {
            sum += Character.getNumericValue(ruc.charAt(i)) * WEIGHTS[i];
        }
        var checkDigit = 11 - (sum % 11);
        return switch (checkDigit) {
            case 10 -> 0;
            case 11 -> 1;
            default -> checkDigit;
        };
    }
}
