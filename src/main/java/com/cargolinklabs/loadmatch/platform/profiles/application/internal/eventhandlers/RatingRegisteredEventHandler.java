package com.cargolinklabs.loadmatch.platform.profiles.application.internal.eventhandlers;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.CarrierRepository;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.ShipperRepository;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.events.RatingRegisteredEvent;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Event handler that updates shipper or carrier reputation when a rating is registered
 */
@Service
public class RatingRegisteredEventHandler {
    private final ShipperRepository shipperRepository;
    private final CarrierRepository carrierRepository;

    public RatingRegisteredEventHandler(ShipperRepository shipperRepository,
                                        CarrierRepository carrierRepository) {
        this.shipperRepository = shipperRepository;
        this.carrierRepository = carrierRepository;
    }

    @EventListener
    public void on(RatingRegisteredEvent event) {
        if (event.raterType() == RaterType.SHIPPER) {
            carrierRepository.findById(new CarrierId(event.rateeId())).ifPresent(carrier -> {
                carrier.registerNewRating(event.score());
                carrierRepository.save(carrier);
            });
            return;
        }
        shipperRepository.findById(new ShipperId(event.rateeId())).ifPresent(shipper -> {
            shipper.registerNewRating(event.score());
            shipperRepository.save(shipper);
        });
    }
}

