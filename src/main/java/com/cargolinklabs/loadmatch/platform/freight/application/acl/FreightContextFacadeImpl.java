package com.cargolinklabs.loadmatch.platform.freight.application.acl;

import com.cargolinklabs.loadmatch.platform.freight.application.commandservices.LoadRequestCommandService;
import com.cargolinklabs.loadmatch.platform.freight.application.queryservices.LoadRequestQueryService;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.commands.ChangeLoadRequestStatusCommand;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestByIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetPublishedLoadRequestsQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestStatus;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.acl.FreightContextFacade;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.acl.PublishedLoadRequestSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Freight Publishing context facade implementation that delegates to the local command and query services
 */
@Service
public class FreightContextFacadeImpl implements FreightContextFacade {
    private final LoadRequestCommandService loadRequestCommandService;
    private final LoadRequestQueryService loadRequestQueryService;

    public FreightContextFacadeImpl(LoadRequestCommandService loadRequestCommandService,
                                    LoadRequestQueryService loadRequestQueryService) {
        this.loadRequestCommandService = loadRequestCommandService;
        this.loadRequestQueryService = loadRequestQueryService;
    }

    @Override
    public List<PublishedLoadRequestSnapshot> fetchPublishedLoadRequests() {
        return loadRequestQueryService.handle(new GetPublishedLoadRequestsQuery()).stream()
                .map(FreightContextFacadeImpl::toSnapshot)
                .toList();
    }

    @Override
    public boolean isLoadRequestAvailable(UUID loadRequestId) {
        return findById(loadRequestId).map(LoadRequest::isAvailable).orElse(false);
    }

    @Override
    public Optional<UUID> fetchShipperIdByLoadRequestId(UUID loadRequestId) {
        return findById(loadRequestId).map(loadRequest -> loadRequest.getShipperId().shipperId());
    }

    @Override
    public boolean markLoadRequestAsAssigned(UUID loadRequestId) {
        return changeStatus(loadRequestId, LoadRequestStatus.ASSIGNED);
    }

    @Override
    public boolean markLoadRequestInTransit(UUID loadRequestId) {
        return changeStatus(loadRequestId, LoadRequestStatus.IN_TRANSIT);
    }

    @Override
    public boolean markLoadRequestAsDelivered(UUID loadRequestId) {
        return changeStatus(loadRequestId, LoadRequestStatus.DELIVERED);
    }

    private Optional<LoadRequest> findById(UUID loadRequestId) {
        return loadRequestQueryService.handle(new GetLoadRequestByIdQuery(new LoadRequestId(loadRequestId)));
    }

    private boolean changeStatus(UUID loadRequestId, LoadRequestStatus targetStatus) {
        return loadRequestCommandService.handle(
                new ChangeLoadRequestStatusCommand(new LoadRequestId(loadRequestId), targetStatus)).isSuccess();
    }

    private static PublishedLoadRequestSnapshot toSnapshot(LoadRequest loadRequest) {
        var origin = loadRequest.getRoute().origin();
        var destination = loadRequest.getRoute().destination();
        return new PublishedLoadRequestSnapshot(
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
                loadRequest.getCargoType(),
                loadRequest.getOfferedRate().amount(),
                loadRequest.getOfferedRate().currency(),
                loadRequest.getPickupAt(),
                loadRequest.isUrgent());
    }
}
