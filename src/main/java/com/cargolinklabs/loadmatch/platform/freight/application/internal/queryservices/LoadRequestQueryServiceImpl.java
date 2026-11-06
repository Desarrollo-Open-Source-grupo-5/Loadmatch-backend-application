package com.cargolinklabs.loadmatch.platform.freight.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.freight.application.queryservices.LoadRequestQueryService;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestByIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetPublishedLoadRequestsQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.repositories.LoadRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Load request query service implementation
 */
@Service
public class LoadRequestQueryServiceImpl implements LoadRequestQueryService {
    private final LoadRequestRepository loadRequestRepository;

    public LoadRequestQueryServiceImpl(LoadRequestRepository loadRequestRepository) {
        this.loadRequestRepository = loadRequestRepository;
    }

    @Override
    public Optional<LoadRequest> handle(GetLoadRequestByIdQuery query) {
        return loadRequestRepository.findById(query.loadRequestId());
    }

    @Override
    public List<LoadRequest> handle(GetLoadRequestsByShipperIdQuery query) {
        return loadRequestRepository.findByShipperId(query.shipperId());
    }

    @Override
    public List<LoadRequest> handle(GetPublishedLoadRequestsQuery query) {
        return loadRequestRepository.findPublished();
    }
}
