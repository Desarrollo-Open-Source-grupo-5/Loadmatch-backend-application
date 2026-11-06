package com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;

/**
 * Command to update the editable data of a shipper profile
 * <p>
 *     The RUC and the business name are not part of this command because they can only be corrected through support.
 * </p>
 */
public record UpdateShipperProfileCommand(
        ShipperId shipperId,
        String contactName,
        String phoneNumber,
        String street,
        String district,
        String province,
        String department) {

    public UpdateShipperProfileCommand {
        if (shipperId == null) {
            throw new IllegalArgumentException("shipperId cannot be null");
        }
    }
}
