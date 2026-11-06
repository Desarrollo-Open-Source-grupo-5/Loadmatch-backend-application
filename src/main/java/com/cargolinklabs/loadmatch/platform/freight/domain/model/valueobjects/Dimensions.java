package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

/**
 * Dimensions value object of a load, in meters
 *
 * @param lengthM the length
 * @param widthM the width
 * @param heightM the height
 */
public record Dimensions(double lengthM, double widthM, double heightM) {
    public Dimensions {
        if (lengthM <= 0 || widthM <= 0 || heightM <= 0) {
            throw new IllegalArgumentException("Dimensions must be greater than zero");
        }
    }

    /**
     * @return the volume in cubic meters
     */
    public double volumeM3() {
        return lengthM * widthM * heightM;
    }
}
