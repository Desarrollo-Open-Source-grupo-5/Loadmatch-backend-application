package com.cargolinklabs.loadmatch.platform.matching.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.freight.interfaces.acl.PublishedLoadRequestSnapshot;
import com.cargolinklabs.loadmatch.platform.matching.application.internal.outboundservices.acl.ExternalFreightService;
import com.cargolinklabs.loadmatch.platform.matching.application.queryservices.AvailableLoadQueryService;
import com.cargolinklabs.loadmatch.platform.matching.domain.model.queries.GetAvailableLoadsNearbyQuery;
import com.cargolinklabs.loadmatch.platform.matching.domain.model.readmodels.AvailableLoad;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Available load query service implementation
 * <p>
 *     Uses the Haversine distance between the carrier and the pickup point of each published load.
 * </p>
 */
@Service
public class AvailableLoadQueryServiceImpl implements AvailableLoadQueryService {
    private final ExternalFreightService externalFreightService;

    public AvailableLoadQueryServiceImpl(ExternalFreightService externalFreightService) {
        this.externalFreightService = externalFreightService;
    }

    @Override
    public List<AvailableLoad> handle(GetAvailableLoadsNearbyQuery query) {
        var carrierLocation = query.criteria().origin();
        return externalFreightService.fetchPublishedLoadRequests().stream()
                .map(loadRequest -> toAvailableLoad(loadRequest, carrierLocation))
                .filter(availableLoad -> availableLoad.distanceToCarrierKm() <= query.criteria().radiusKm())
                .sorted(Comparator.comparingDouble(AvailableLoad::distanceToCarrierKm))
                .toList();
    }

    private static AvailableLoad toAvailableLoad(PublishedLoadRequestSnapshot loadRequest, Coordinates carrierLocation) {
        var origin = new Coordinates(loadRequest.originLatitude(), loadRequest.originLongitude());
        var destination = new Coordinates(loadRequest.destinationLatitude(), loadRequest.destinationLongitude());
        return new AvailableLoad(
                loadRequest.loadRequestId(),
                loadRequest.shipperId(),
                loadRequest.vehicleTypeId(),
                loadRequest.originAddress(),
                loadRequest.originDistrict(),
                loadRequest.originLatitude(),
                loadRequest.originLongitude(),
                loadRequest.destinationAddress(),
                loadRequest.destinationDistrict(),
                loadRequest.destinationLatitude(),
                loadRequest.destinationLongitude(),
                loadRequest.weightKg(),
                loadRequest.cargoType(),
                loadRequest.offeredRateAmount(),
                loadRequest.currency(),
                loadRequest.pickupAt(),
                loadRequest.urgent(),
                round(carrierLocation.distanceTo(origin)),
                round(origin.distanceTo(destination)));
    }

    private static double round(double kilometers) {
        return Math.round(kilometers * 100.0) / 100.0;
    }
}
