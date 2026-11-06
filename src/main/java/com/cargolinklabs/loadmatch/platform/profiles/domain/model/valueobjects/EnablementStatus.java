package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

/**
 * Enablement status of a carrier
 * <p>
 *     A carrier starts PENDING until its documents are validated, and only an ENABLED carrier can accept trips.
 * </p>
 */
public enum EnablementStatus {
    PENDING,
    ENABLED,
    SUSPENDED
}
