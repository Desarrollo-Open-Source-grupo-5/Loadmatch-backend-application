package com.cargolinklabs.loadmatch.platform.trip.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.trip.application.commandservices.TripCommandService;
import com.cargolinklabs.loadmatch.platform.trip.application.internal.outboundservices.acl.ExternalFreightService;
import com.cargolinklabs.loadmatch.platform.trip.application.internal.outboundservices.acl.ExternalProfilesService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.AcceptLoadRequestCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.CompleteUnconfirmedDeliveriesCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ConfirmDeliveryCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ObserveDeliveryCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ReportIncidentCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.UpdateTripStatusCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.entities.Incident;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentEvidence;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.VehicleId;
import com.cargolinklabs.loadmatch.platform.trip.domain.repositories.TripRepository;
import com.cargolinklabs.loadmatch.platform.trip.domain.services.EvidenceStorageService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Trip command service implementation
 */
@Service
public class TripCommandServiceImpl implements TripCommandService {
    private static final int MAX_EVIDENCE_BYTES = 5 * 1024 * 1024;
    private static final byte[] JPEG_SIGNATURE = HexFormat.of().parseHex("ffd8ff");
    private static final byte[] PNG_SIGNATURE = HexFormat.of().parseHex("89504e470d0a1a0a");
    private static final byte[] PDF_SIGNATURE = HexFormat.of().parseHex("255044462d");

    private final TripRepository tripRepository;
    private final ExternalProfilesService externalProfilesService;
    private final ExternalFreightService externalFreightService;
    private final EvidenceStorageService evidenceStorageService;
    private final MessageSource messageSource;

    public TripCommandServiceImpl(TripRepository tripRepository, ExternalProfilesService externalProfilesService,
                                  ExternalFreightService externalFreightService,
                                  EvidenceStorageService evidenceStorageService, MessageSource messageSource) {
        this.tripRepository = tripRepository;
        this.externalProfilesService = externalProfilesService;
        this.externalFreightService = externalFreightService;
        this.evidenceStorageService = evidenceStorageService;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public Result<Trip, ApplicationError> handle(AcceptLoadRequestCommand command) {
        var carrierId = new CarrierId(command.carrierId());
        if (!externalProfilesService.isCarrierEnabled(carrierId)) {
            return Result.failure(ApplicationError.businessRuleViolation("carrier-enabled",
                    "The carrier must complete the validation of its profile before accepting trips"));
        }
        var loadRequestId = new LoadRequestId(command.loadRequestId());
        if (!externalFreightService.isLoadRequestAvailable(loadRequestId)) {
            return Result.failure(ApplicationError.conflict("Load_Request",
                    "The load request %s is no longer available".formatted(command.loadRequestId())));
        }
        var shipperId = externalFreightService.fetchShipperId(loadRequestId);
        if (shipperId.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Load_Request", command.loadRequestId().toString()));
        }
        var trip = new Trip(loadRequestId, shipperId.get(), carrierId, new VehicleId(command.vehicleId()));
        trip.onCreated();
        var savedTrip = tripRepository.save(trip);
        externalFreightService.markLoadRequestAsAssigned(loadRequestId);
        return Result.success(savedTrip);
    }

    @Override
    @Transactional
    public Result<Trip, ApplicationError> handle(UpdateTripStatusCommand command) {
        var trip = tripRepository.findByIdForUpdate(command.tripId());
        if (trip.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Trip", command.tripId().tripId().toString()));
        }
        try {
            trip.get().updateStatus(command.status(), command.description(), command.evidenceUrl());
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("trip-status-transition", e.getMessage()));
        }
        var savedTrip = tripRepository.save(trip.get());
        if (command.status() == TripStatus.CARGO_PICKED_UP) {
            externalFreightService.markLoadRequestInTransit(savedTrip.getLoadRequestId());
        }
        if (command.status() == TripStatus.DELIVERED) {
            externalFreightService.markLoadRequestAsDelivered(savedTrip.getLoadRequestId());
        }
        return Result.success(savedTrip);
    }

    @Override
    @Transactional
    public Result<Incident, ApplicationError> handle(ReportIncidentCommand command) {
        final byte[] content;
        try {
            content = decodeEvidence(command);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("incident-evidence",
                    messageSource.getMessage("trip.incident.evidence.invalid", null, LocaleContextHolder.getLocale())));
        }

        var trip = tripRepository.findByIdForUpdate(command.tripId())
                .filter(value -> externalProfilesService.isAssignedCarrier(value, command.reporterEmail()));
        if (trip.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Trip", command.tripId().tripId().toString()));
        }

        var incidentId = UUID.randomUUID();
        var evidenceUrl = "/api/v1/trips/%s/incidents/%s/evidence".formatted(command.tripId().tripId(), incidentId);
        final Incident incident;
        try {
            incident = trip.get().reportIncident(incidentId, command.type(), command.description(), evidenceUrl);
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("trip-incident-status",
                    messageSource.getMessage("trip.incident.status.invalid", null, LocaleContextHolder.getLocale())));
        }

        tripRepository.save(trip.get());
        evidenceStorageService.store(incidentId,
                new IncidentEvidence(command.fileName().trim(), command.contentType(), content));
        return Result.success(incident);
    }

    private static byte[] decodeEvidence(ReportIncidentCommand command) {
        byte[] signature = switch (command.contentType()) {
            case "image/jpeg" -> JPEG_SIGNATURE;
            case "image/png" -> PNG_SIGNATURE;
            case "application/pdf" -> PDF_SIGNATURE;
            default -> throw new IllegalArgumentException("Unsupported evidence type");
        };
        if (command.fileBase64().length() > ((MAX_EVIDENCE_BYTES + 2) / 3) * 4) {
            throw new IllegalArgumentException("Evidence exceeds the size limit");
        }
        var content = Base64.getDecoder().decode(command.fileBase64());
        if (content.length > MAX_EVIDENCE_BYTES || content.length < signature.length
                || !Arrays.equals(content, 0, signature.length, signature, 0, signature.length)) {
            throw new IllegalArgumentException("Invalid evidence content");
        }
        return content;
    }

    @Override
    public Result<Trip, ApplicationError> handle(ConfirmDeliveryCommand command) {
        return applyShipperResponse(command.tripId(), Trip::confirmReceipt);
    }

    @Override
    public Result<Trip, ApplicationError> handle(ObserveDeliveryCommand command) {
        return applyShipperResponse(command.tripId(), trip -> trip.disputeDelivery(command.observation()));
    }

    @Override
    public int handle(CompleteUnconfirmedDeliveriesCommand command) {
        var limit = LocalDateTime.now().minusHours(CompleteUnconfirmedDeliveriesCommand.CONFIRMATION_WINDOW_HOURS);
        var trips = tripRepository.findDeliveredBefore(limit);
        trips.forEach(trip -> {
            trip.completeWithoutResponse();
            tripRepository.save(trip);
        });
        return trips.size();
    }

    private Result<Trip, ApplicationError> applyShipperResponse(TripId tripId, Consumer<Trip> response) {
        var trip = tripRepository.findById(tripId);
        if (trip.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Trip", tripId.tripId().toString()));
        }
        try {
            response.accept(trip.get());
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("delivery-confirmation", e.getMessage()));
        }
        return Result.success(tripRepository.save(trip.get()));
    }
}
