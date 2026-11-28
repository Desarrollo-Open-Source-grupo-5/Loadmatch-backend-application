package com.cargolinklabs.loadmatch.platform.rating.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.rating.application.commandservices.RatingCommandService;
import com.cargolinklabs.loadmatch.platform.rating.application.internal.outboundservices.acl.ExternalPaymentService;
import com.cargolinklabs.loadmatch.platform.rating.application.internal.outboundservices.acl.ExternalTripService;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.commands.RegisterRatingCommand;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.Comment;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.Score;
import com.cargolinklabs.loadmatch.platform.rating.domain.repositories.RatingRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command service implementation for Rating
 */
@Service
public class RatingCommandServiceImpl implements RatingCommandService {
    private final RatingRepository ratingRepository;
    private final ExternalTripService externalTripService;
    private final ExternalPaymentService externalPaymentService;

    public RatingCommandServiceImpl(RatingRepository ratingRepository,
                                    ExternalTripService externalTripService,
                                    ExternalPaymentService externalPaymentService) {
        this.ratingRepository = ratingRepository;
        this.externalTripService = externalTripService;
        this.externalPaymentService = externalPaymentService;
    }

    @Override
    @Transactional
    public Result<Rating, ApplicationError> handle(RegisterRatingCommand command) {
        var tripOpt = externalTripService.fetchTripById(command.tripId());
        if (tripOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("TRIP", command.tripId().toString()));
        }

        var trip = tripOpt.get();
        if (!"COMPLETED".equalsIgnoreCase(trip.status())) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "TRIP_NOT_COMPLETED",
                    "Ratings can only be registered for completed trips"));
        }

        var rateeId = command.raterType() == RaterType.SHIPPER ? trip.carrierId() : trip.shipperId();
        var expectedRaterId = command.raterType() == RaterType.SHIPPER ? trip.shipperId() : trip.carrierId();

        if (!expectedRaterId.equals(command.raterId())) {
            return Result.failure(ApplicationError.forbidden(
                    "RATER_MISMATCH",
                    "Only participants of the trip can submit a rating"));
        }

        if (command.raterType() == RaterType.CARRIER && !externalPaymentService.isTripPaid(command.tripId())) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "TRIP_NOT_PAID",
                    "Carriers can rate shippers only after the trip is paid"));
        }

        if (ratingRepository.existsByTripIdAndRaterId(command.tripId(), command.raterId())) {
            return Result.failure(ApplicationError.conflict(
                    "RATING",
                    "You have already rated this trip"));
        }

        try {
            var rating = new Rating(
                    command.tripId(),
                    command.raterId(),
                    rateeId,
                    command.raterType(),
                    new Score(command.score()),
                    new Comment(command.comment())
            );
            rating.onRegistered();
            var saved = ratingRepository.save(rating);
            return Result.success(saved);
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("rating", ex.getMessage()));
        }
    }
}
