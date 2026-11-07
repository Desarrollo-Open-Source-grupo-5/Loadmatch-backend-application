package com.cargolinklabs.loadmatch.platform.profiles.domain.model.events;

import java.util.UUID;

/**
 * Domain event raised when a carrier is enabled to accept trips
 *
 * @param carrierId the carrier identifier
 */
public record CarrierEnabledEvent(UUID carrierId) {
}
