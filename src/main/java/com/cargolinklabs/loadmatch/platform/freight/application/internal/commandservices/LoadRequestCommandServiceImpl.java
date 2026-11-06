package com.cargolinklabs.loadmatch.platform.freight.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.freight.application.commandservices.LoadRequestCommandService;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.commands.ChangeLoadRequestStatusCommand;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.commands.CreateLoadRequestCommand;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.freight.domain.repositories.LoadRequestRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;
import org.springframework.stereotype.Service;

/**
 * Load request command service implementation
 */
@Service
public class LoadRequestCommandServiceImpl implements LoadRequestCommandService {
    private final LoadRequestRepository loadRequestRepository;

    public LoadRequestCommandServiceImpl(LoadRequestRepository loadRequestRepository) {
        this.loadRequestRepository = loadRequestRepository;
    }

    @Override
    public Result<LoadRequest, ApplicationError> handle(CreateLoadRequestCommand command) {
        var route = new Route(
                new Location(command.originAddress(), command.originDistrict(),
                        new Coordinates(command.originLatitude(), command.originLongitude())),
                new Location(command.destinationAddress(), command.destinationDistrict(),
                        new Coordinates(command.destinationLatitude(), command.destinationLongitude())));
        var loadRequest = new LoadRequest(
                new ShipperId(command.shipperId()),
                new VehicleTypeId(command.vehicleTypeId()),
                route,
                command.weightKg(),
                new Dimensions(command.lengthM(), command.widthM(), command.heightM()),
                command.cargoType(),
                Money.ofSoles(command.offeredRateAmount()),
                command.pickupAt());
        loadRequest.publish();
        return Result.success(loadRequestRepository.save(loadRequest));
    }

    @Override
    public Result<LoadRequest, ApplicationError> handle(ChangeLoadRequestStatusCommand command) {
        var loadRequest = loadRequestRepository.findById(command.loadRequestId());
        if (loadRequest.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "Load_Request", command.loadRequestId().loadRequestId().toString()));
        }
        try {
            switch (command.targetStatus()) {
                case ASSIGNED -> loadRequest.get().markAsAssigned();
                case IN_TRANSIT -> loadRequest.get().markInTransit();
                case DELIVERED -> loadRequest.get().markAsDelivered();
                default -> {
                    return Result.failure(ApplicationError.validationError(
                            "targetStatus", "Status %s cannot be set from another context".formatted(command.targetStatus())));
                }
            }
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("load-request-status", e.getMessage()));
        }
        return Result.success(loadRequestRepository.save(loadRequest.get()));
    }
}
