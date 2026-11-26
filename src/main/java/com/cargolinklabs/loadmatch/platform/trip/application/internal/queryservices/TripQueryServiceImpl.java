package com.cargolinklabs.loadmatch.platform.trip.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.trip.application.internal.outboundservices.acl.ExternalProfilesService;
import com.cargolinklabs.loadmatch.platform.trip.application.queryservices.TripQueryService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.entities.Incident;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetIncidentEvidenceQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripIncidentsQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentEvidence;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.domain.repositories.TripRepository;
import com.cargolinklabs.loadmatch.platform.trip.domain.services.EvidenceStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Trip query service implementation
 */
@Service
public class TripQueryServiceImpl implements TripQueryService {
    private final TripRepository tripRepository;
    private final ExternalProfilesService externalProfilesService;
    private final EvidenceStorageService evidenceStorageService;

    public TripQueryServiceImpl(TripRepository tripRepository, ExternalProfilesService externalProfilesService,
                                EvidenceStorageService evidenceStorageService) {
        this.tripRepository = tripRepository;
        this.externalProfilesService = externalProfilesService;
        this.evidenceStorageService = evidenceStorageService;
    }

    @Override
    public Optional<Trip> handle(GetTripByIdQuery query) {
        return tripRepository.findById(query.tripId());
    }

    @Override
    public List<Trip> handle(GetTripsByCarrierIdQuery query) {
        return tripRepository.findByCarrierId(query.carrierId());
    }

    @Override
    public List<Trip> handle(GetTripsByShipperIdQuery query) {
        return tripRepository.findByShipperId(query.shipperId());
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<Incident>, ApplicationError> handle(GetTripIncidentsQuery query) {
        var trip = findTripForParticipant(query.tripId(), query.requesterEmail());
        if (trip.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Trip", query.tripId().tripId().toString()));
        }
        return Result.success(trip.get().getIncidents());
    }

    @Override
    @Transactional(readOnly = true)
    public Result<IncidentEvidence, ApplicationError> handle(GetIncidentEvidenceQuery query) {
        var trip = findTripForParticipant(query.tripId(), query.requesterEmail());
        if (trip.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Trip", query.tripId().tripId().toString()));
        }
        var evidence = trip.get().getIncidents().stream()
                .filter(incident -> incident.id().equals(query.incidentId()))
                .findFirst()
                .flatMap(incident -> evidenceStorageService.findByIncidentId(incident.id()));
        if (evidence.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Incident_Evidence", query.incidentId().toString()));
        }
        return Result.success(evidence.get());
    }

    private Optional<Trip> findTripForParticipant(TripId tripId, String email) {
        return tripRepository.findById(tripId).filter(trip -> externalProfilesService.isParticipant(trip, email));
    }
}
