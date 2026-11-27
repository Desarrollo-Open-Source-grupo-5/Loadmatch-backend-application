package com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentId;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.payment.domain.repositories.PaymentRepository;
import com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.assemblers.PaymentPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.repositories.PaymentPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * JPA adapter of the payment repository port
 */
@Repository
@Transactional(readOnly = true)
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentPersistenceRepository paymentPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public PaymentRepositoryImpl(PaymentPersistenceRepository paymentPersistenceRepository,
                                 DomainEventPublisher domainEventPublisher) {
        this.paymentPersistenceRepository = paymentPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<Payment> findById(PaymentId id) {
        return paymentPersistenceRepository.findById(id.paymentId())
                .map(PaymentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Payment> findByTripId(TripId tripId) {
        return paymentPersistenceRepository.findByTripId(tripId.tripId())
                .map(PaymentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        var saved = paymentPersistenceRepository.save(PaymentPersistenceAssembler.toPersistenceFromDomain(payment));
        domainEventPublisher.publishAndClearEvents(payment);
        return PaymentPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
