package com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.commands.RegisterRatingCommand;
import com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.resources.RegisterRatingResource;

/**
 * Assembler translating RegisterRatingResource to RegisterRatingCommand
 */
public final class RegisterRatingCommandFromResourceAssembler {

    private RegisterRatingCommandFromResourceAssembler() {
    }

    public static RegisterRatingCommand toCommandFromResource(RegisterRatingResource resource) {
        return new RegisterRatingCommand(
                resource.tripId(),
                resource.raterId(),
                resource.raterType(),
                resource.score(),
                resource.comment()
        );
    }
}

