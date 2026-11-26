package com.cargolinklabs.loadmatch.platform.trip.application.queryservices;

import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.entities.Incident;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetIncidentEvidenceQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripIncidentsQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentEvidence;

import java.util.List;
import java.util.Optional;

/**
 * Trip query service
 */
public interface TripQueryService {
    Optional<Trip> handle(GetTripByIdQuery query);

    List<Trip> handle(GetTripsByCarrierIdQuery query);

    List<Trip> handle(GetTripsByShipperIdQuery query);

    Result<List<Incident>, ApplicationError> handle(GetTripIncidentsQuery query);

    Result<IncidentEvidence, ApplicationError> handle(GetIncidentEvidenceQuery query);
}
