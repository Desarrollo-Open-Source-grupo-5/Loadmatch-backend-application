package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

/**
 * Status of a load request
 */
public enum LoadRequestStatus {
    DRAFT,
    PUBLISHED,
    ASSIGNED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED
}
