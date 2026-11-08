package com.cargolinklabs.loadmatch.platform.profiles.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.profiles.application.queryservices.CarrierQueryService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByUserIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.CarrierRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Carrier query service implementation
 */
@Service
public class CarrierQueryServiceImpl implements CarrierQueryService {
    private final CarrierRepository carrierRepository;

    public CarrierQueryServiceImpl(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @Override
    public Optional<Carrier> handle(GetCarrierByIdQuery query) {
        return carrierRepository.findById(query.carrierId());
    }

    @Override
    public Optional<Carrier> handle(GetCarrierByUserIdQuery query) {
        return carrierRepository.findByUserId(query.userId());
    }
}
