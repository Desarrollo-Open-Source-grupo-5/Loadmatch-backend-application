package com.cargolinklabs.loadmatch.platform.shared.infrastructure.events;

import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Shared infrastructure service responsible for publishing and clearing domain events
 */
@Component
@NullMarked
public class DomainEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;


    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Dispatches all domain events registered on the aggregate root through the Spring application event publisher
     */
    public <T extends AbstractDomainAggregateRoot<T>> void publishAndClearEvents(
            @Nullable T aggregate) {
        if (aggregate == null) {
            return;
        }
        aggregate.domainEvents().forEach(applicationEventPublisher::publishEvent);
        aggregate.clearDomainEvents();
    }
}

