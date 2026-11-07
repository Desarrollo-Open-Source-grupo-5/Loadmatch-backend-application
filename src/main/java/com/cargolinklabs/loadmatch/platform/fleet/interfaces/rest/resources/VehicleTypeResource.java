package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources;

import java.util.UUID;

/**
 * Vehicle type response resource
 */
public record VehicleTypeResource(UUID id, String name, String description, double maxWeightKg) {
}
