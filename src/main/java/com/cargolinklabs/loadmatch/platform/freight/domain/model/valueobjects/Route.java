package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

/**
 * Route value object composed of the origin and the destination of a load
 *
 * @param origin the pickup location
 * @param destination the delivery location
 */
public record Route(Location origin, Location destination) {
    public Route {
        if (origin == null || destination == null) {
            throw new IllegalArgumentException("Origin and destination cannot be null");
        }
        if (origin.coordinates().equals(destination.coordinates())) {
            throw new IllegalArgumentException("Origin and destination must be different");
        }
    }
}
