package com.cargolinklabs.loadmatch.platform.matching.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.matching.domain.model.readmodels.AvailableLoad;
import com.cargolinklabs.loadmatch.platform.matching.interfaces.rest.resources.AvailableLoadResource;

/**
 * Assembler that translates an available load read model into its response resource
 */
public final class AvailableLoadResourceFromReadModelAssembler {

    private AvailableLoadResourceFromReadModelAssembler() {
    }

    public static AvailableLoadResource toResourceFromReadModel(AvailableLoad availableLoad) {
        return new AvailableLoadResource(
                availableLoad.loadRequestId(),
                availableLoad.shipperId(),
                availableLoad.vehicleTypeId(),
                availableLoad.originAddress(),
                availableLoad.originDistrict(),
                availableLoad.originLatitude(),
                availableLoad.originLongitude(),
                availableLoad.destinationAddress(),
                availableLoad.destinationDistrict(),
                availableLoad.destinationLatitude(),
                availableLoad.destinationLongitude(),
                availableLoad.weightKg(),
                availableLoad.cargoType(),
                availableLoad.offeredRateAmount(),
                availableLoad.currency(),
                availableLoad.pickupAt(),
                availableLoad.urgent(),
                availableLoad.distanceToCarrierKm(),
                availableLoad.routeDistanceKm());
    }
}
