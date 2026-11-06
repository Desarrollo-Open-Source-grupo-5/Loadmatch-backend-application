package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.ShipperResource;

/**
 * Assembler that translates a shipper into its response resource
 */
public final class ShipperResourceFromEntityAssembler {

    private ShipperResourceFromEntityAssembler() {
    }

    public static ShipperResource toResourceFromEntity(Shipper shipper) {
        return new ShipperResource(
                shipper.getId().shipperId(),
                shipper.getUserId().userId(),
                shipper.getRuc().ruc(),
                shipper.getBusinessName(),
                shipper.getContactName(),
                shipper.getPhoneNumber().phoneNumber(),
                shipper.getFiscalAddress().street(),
                shipper.getFiscalAddress().district(),
                shipper.getFiscalAddress().province(),
                shipper.getFiscalAddress().department());
    }
}
