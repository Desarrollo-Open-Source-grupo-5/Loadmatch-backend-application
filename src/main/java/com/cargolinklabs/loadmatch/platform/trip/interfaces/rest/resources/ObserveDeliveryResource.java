package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Delivery observation resource sent by the shipper
 *
 * @param observation the reason of the observation
 */
public record ObserveDeliveryResource(@NotBlank(message = "{validation.not-blank}") String observation) {
}
