package com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.resources.LoadRequestResource;

/**
 * Assembler that translates a load request into its response resource
 */
public final class LoadRequestResourceFromEntityAssembler {

    private LoadRequestResourceFromEntityAssembler() {
    }

    public static LoadRequestResource toResourceFromEntity(LoadRequest loadRequest) {
        var origin = loadRequest.getRoute().origin();
        var destination = loadRequest.getRoute().destination();
        return new LoadRequestResource(
                loadRequest.getId().loadRequestId(),
                loadRequest.getShipperId().shipperId(),
                loadRequest.getVehicleTypeId().vehicleTypeId(),
                origin.address(),
                origin.district(),
                origin.coordinates().latitude(),
                origin.coordinates().longitude(),
                destination.address(),
                destination.district(),
                destination.coordinates().latitude(),
                destination.coordinates().longitude(),
                loadRequest.getWeightKg(),
                loadRequest.getDimensions().lengthM(),
                loadRequest.getDimensions().widthM(),
                loadRequest.getDimensions().heightM(),
                loadRequest.getCargoType(),
                loadRequest.getOfferedRate().amount(),
                loadRequest.getOfferedRate().currency(),
                loadRequest.getStatus().name(),
                loadRequest.getPickupAt(),
                loadRequest.getPublishedAt(),
                loadRequest.isUrgent());
    }
}
