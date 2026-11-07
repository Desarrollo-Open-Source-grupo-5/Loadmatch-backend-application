package com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;

/**
 * Command to update the contact data of a carrier profile
 */
public record UpdateCarrierProfileCommand(
        CarrierId carrierId,
        String phoneNumber,
        String operatingZone,
        String photoUrl) {

    public UpdateCarrierProfileCommand {
        if (carrierId == null) {
            throw new IllegalArgumentException("carrierId cannot be null");
        }
    }
}
