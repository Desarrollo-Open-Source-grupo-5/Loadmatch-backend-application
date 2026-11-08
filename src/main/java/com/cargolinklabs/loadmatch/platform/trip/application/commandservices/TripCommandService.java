package com.cargolinklabs.loadmatch.platform.trip.application.commandservices;

import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.AcceptLoadRequestCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.CompleteUnconfirmedDeliveriesCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ConfirmDeliveryCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ObserveDeliveryCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.UpdateTripStatusCommand;

/**
 * Trip command service
 */
public interface TripCommandService {
    /**
     * Accepts a load request on behalf of an enabled carrier and creates the trip
     * @param command the accept load request command
     * @return the created trip or an application error
     */
    Result<Trip, ApplicationError> handle(AcceptLoadRequestCommand command);

    /**
     * Updates the status of a trip and keeps the load request of the shipper in sync
     * @param command the update trip status command
     * @return the updated trip or an application error
     */
    Result<Trip, ApplicationError> handle(UpdateTripStatusCommand command);

    /**
     * The shipper confirms that the delivery was received as agreed
     * @param command the confirm delivery command
     * @return the completed trip or an application error
     */
    Result<Trip, ApplicationError> handle(ConfirmDeliveryCommand command);

    /**
     * The shipper registers an observation about the delivery
     * @param command the observe delivery command
     * @return the disputed trip or an application error
     */
    Result<Trip, ApplicationError> handle(ObserveDeliveryCommand command);

    /**
     * Completes the delivered trips the shipper did not confirm nor observe within 24 hours
     * @param command the complete unconfirmed deliveries command
     * @return the number of completed trips
     */
    int handle(CompleteUnconfirmedDeliveriesCommand command);
}
