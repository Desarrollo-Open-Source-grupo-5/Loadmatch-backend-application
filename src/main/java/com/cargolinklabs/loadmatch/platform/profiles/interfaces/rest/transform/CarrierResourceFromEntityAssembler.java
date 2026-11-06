package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.CarrierResource;

/**
 * Assembler that translates a carrier into its response resource
 */
public final class CarrierResourceFromEntityAssembler {

    private CarrierResourceFromEntityAssembler() {
    }

    public static CarrierResource toResourceFromEntity(Carrier carrier) {
        return new CarrierResource(
                carrier.getId().carrierId(),
                carrier.getUserId().userId(),
                carrier.getFirstNames(),
                carrier.getLastNames(),
                carrier.getFullName(),
                carrier.getDni().dni(),
                carrier.getLicense().number(),
                carrier.getLicense().category(),
                carrier.getLicense().expirationDate(),
                carrier.getPhoneNumber().phoneNumber(),
                carrier.getOperatingZone(),
                carrier.getPhotoUrl(),
                carrier.getEnablementStatus().name(),
                carrier.canAcceptTrips());
    }
}
