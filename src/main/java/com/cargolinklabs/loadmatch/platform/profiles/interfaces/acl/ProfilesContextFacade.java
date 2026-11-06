package com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl;

import java.util.UUID;

/**
 * Profiles context facade
 * <p>
 *     Entry point that other bounded contexts use to query shippers and carriers by identity.
 * </p>
 */
public interface ProfilesContextFacade {
    /**
     * @param shipperId the shipper identifier
     * @return true when the shipper exists
     */
    boolean existsShipperById(UUID shipperId);

    /**
     * @param carrierId the carrier identifier
     * @return true when the carrier exists
     */
    boolean existsCarrierById(UUID carrierId);

    /**
     * @param carrierId the carrier identifier
     * @return true when the carrier exists and is enabled to accept trips
     */
    boolean isCarrierEnabled(UUID carrierId);
}
