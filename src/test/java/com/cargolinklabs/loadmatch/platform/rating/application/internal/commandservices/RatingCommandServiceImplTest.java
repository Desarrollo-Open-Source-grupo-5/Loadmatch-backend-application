package com.cargolinklabs.loadmatch.platform.rating.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.rating.application.internal.outboundservices.acl.ExternalPaymentService;
import com.cargolinklabs.loadmatch.platform.rating.application.internal.outboundservices.acl.ExternalTripService;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.commands.RegisterRatingCommand;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;
import com.cargolinklabs.loadmatch.platform.rating.domain.repositories.RatingRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RatingCommandServiceImplTest {

    @Test
    void carrierCannotRateShipperWhenTripIsNotPaid() {
        var repo = mock(RatingRepository.class);
        var tripService = mock(ExternalTripService.class);
        var paymentService = mock(ExternalPaymentService.class);
        var service = new RatingCommandServiceImpl(repo, tripService, paymentService);

        var tripId = UUID.randomUUID();
        var shipperId = UUID.randomUUID();
        var carrierId = UUID.randomUUID();
        when(tripService.fetchTripById(tripId))
                .thenReturn(Optional.of(new ExternalTripService.TripData(tripId, UUID.randomUUID(), shipperId, carrierId, "COMPLETED")));
        when(paymentService.isTripPaid(tripId)).thenReturn(false);

        var result = service.handle(new RegisterRatingCommand(tripId, carrierId, RaterType.CARRIER, 5, "Good"));
        assertTrue(result instanceof Result.Failure<Rating, ApplicationError>);
    }

    @Test
    void carrierCanRateShipperWhenTripIsCompletedAndPaid() {
        var repo = mock(RatingRepository.class);
        var tripService = mock(ExternalTripService.class);
        var paymentService = mock(ExternalPaymentService.class);
        var service = new RatingCommandServiceImpl(repo, tripService, paymentService);

        var tripId = UUID.randomUUID();
        var shipperId = UUID.randomUUID();
        var carrierId = UUID.randomUUID();
        when(tripService.fetchTripById(tripId))
                .thenReturn(Optional.of(new ExternalTripService.TripData(tripId, UUID.randomUUID(), shipperId, carrierId, "COMPLETED")));
        when(paymentService.isTripPaid(tripId)).thenReturn(true);
        when(repo.existsByTripIdAndRaterId(tripId, carrierId)).thenReturn(false);
        when(repo.save(any(Rating.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.handle(new RegisterRatingCommand(tripId, carrierId, RaterType.CARRIER, 5, "On time payment"));
        assertInstanceOf(Result.Success.class, result);
        var saved = ((Result.Success<Rating, ApplicationError>) result).value();
        assertEquals(shipperId, saved.getRateeId());
    }
}
