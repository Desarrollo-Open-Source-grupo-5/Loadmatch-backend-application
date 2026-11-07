package com.cargolinklabs.loadmatch.platform.profiles.application.acl;

import com.cargolinklabs.loadmatch.platform.profiles.application.queryservices.CarrierQueryService;
import com.cargolinklabs.loadmatch.platform.profiles.application.queryservices.ShipperQueryService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Profiles context facade implementation that delegates to the local query services
 */
@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {
    private final ShipperQueryService shipperQueryService;
    private final CarrierQueryService carrierQueryService;

    public ProfilesContextFacadeImpl(ShipperQueryService shipperQueryService, CarrierQueryService carrierQueryService) {
        this.shipperQueryService = shipperQueryService;
        this.carrierQueryService = carrierQueryService;
    }

    @Override
    public boolean existsShipperById(UUID shipperId) {
        return shipperQueryService.handle(new GetShipperByIdQuery(new ShipperId(shipperId))).isPresent();
    }

    @Override
    public boolean existsCarrierById(UUID carrierId) {
        return carrierQueryService.handle(new GetCarrierByIdQuery(new CarrierId(carrierId))).isPresent();
    }

    @Override
    public boolean isCarrierEnabled(UUID carrierId) {
        return carrierQueryService.handle(new GetCarrierByIdQuery(new CarrierId(carrierId)))
                .map(Carrier::canAcceptTrips)
                .orElse(false);
    }
}
