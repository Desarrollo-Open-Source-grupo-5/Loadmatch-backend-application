package com.cargolinklabs.loadmatch.platform.rating.application.commandservices;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.commands.RegisterRatingCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * Command service interface for Rating
 */
public interface RatingCommandService {
    Result<Rating, ApplicationError> handle(RegisterRatingCommand command);
}
