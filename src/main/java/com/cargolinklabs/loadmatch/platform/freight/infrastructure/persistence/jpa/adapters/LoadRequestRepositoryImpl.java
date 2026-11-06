package com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestStatus;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.freight.domain.repositories.LoadRequestRepository;
import com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.assemblers.LoadRequestPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.repositories.LoadRequestPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA adapter of the load request repository port
 */
@Repository
public class LoadRequestRepositoryImpl implements LoadRequestRepository {
    private final LoadRequestPersistenceRepository loadRequestPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public LoadRequestRepositoryImpl(LoadRequestPersistenceRepository loadRequestPersistenceRepository,
                                     DomainEventPublisher domainEventPublisher) {
        this.loadRequestPersistenceRepository = loadRequestPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<LoadRequest> findById(LoadRequestId id) {
        return loadRequestPersistenceRepository.findById(id.loadRequestId())
                .map(LoadRequestPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<LoadRequest> findByShipperId(ShipperId shipperId) {
        return loadRequestPersistenceRepository.findByShipperIdOrderByCreatedAtDesc(shipperId.shipperId()).stream()
                .map(LoadRequestPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<LoadRequest> findPublished() {
        return loadRequestPersistenceRepository.findByStatusOrderByPublishedAtDesc(LoadRequestStatus.PUBLISHED).stream()
                .map(LoadRequestPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public LoadRequest save(LoadRequest loadRequest) {
        var saved = loadRequestPersistenceRepository.save(LoadRequestPersistenceAssembler.toPersistenceFromDomain(loadRequest));
        domainEventPublisher.publishAndClearEvents(loadRequest);
        return LoadRequestPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
