package com.cargolinklabs.loadmatch.platform.freight.domain.repositories;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.ShipperId;

import java.util.List;
import java.util.Optional;

/**
 * Load request repository port
 */
public interface LoadRequestRepository {
    Optional<LoadRequest> findById(LoadRequestId id);

    List<LoadRequest> findByShipperId(ShipperId shipperId);

    List<LoadRequest> findPublished();

    LoadRequest save(LoadRequest loadRequest);
}
