package com.cargolinklabs.loadmatch.platform.profiles.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.profiles.application.queryservices.ShipperQueryService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByUserIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.ShipperRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Shipper query service implementation
 */
@Service
public class ShipperQueryServiceImpl implements ShipperQueryService {
    private final ShipperRepository shipperRepository;

    public ShipperQueryServiceImpl(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
    }

    @Override
    public Optional<Shipper> handle(GetShipperByIdQuery query) {
        return shipperRepository.findById(query.shipperId());
    }

    @Override
    public Optional<Shipper> handle(GetShipperByUserIdQuery query) {
        return shipperRepository.findByUserId(query.userId());
    }
}
