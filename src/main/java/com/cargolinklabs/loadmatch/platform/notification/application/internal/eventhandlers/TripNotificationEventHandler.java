package com.cargolinklabs.loadmatch.platform.notification.application.internal.eventhandlers;

import com.cargolinklabs.loadmatch.platform.notification.application.commandservices.NotificationCommandService;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.commands.CreateNotificationCommand;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects.NotificationType;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.events.CargoDeliveredAtDestinationEvent;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.events.CargoPickedUpEvent;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.events.IncidentReportedEvent;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.events.TripCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TripNotificationEventHandler {
    private final ProfilesContextFacade profilesContextFacade;
    private final NotificationCommandService notificationCommandService;

    public TripNotificationEventHandler(ProfilesContextFacade profilesContextFacade,
                                        NotificationCommandService notificationCommandService) {
        this.profilesContextFacade = profilesContextFacade;
        this.notificationCommandService = notificationCommandService;
    }

    @EventListener
    public void on(TripCreatedEvent event) {
        notifyShipper(event.shipperId(), NotificationType.LOAD_ACCEPTED,
                "Load accepted", "A carrier accepted your load.", false);
    }

    @EventListener
    public void on(CargoPickedUpEvent event) {
        notifyShipper(event.shipperId(), NotificationType.CARGO_PICKED_UP,
                "Cargo picked up", "The carrier picked up your cargo.", false);
    }

    @EventListener
    public void on(CargoDeliveredAtDestinationEvent event) {
        notifyShipper(event.shipperId(), NotificationType.CARGO_DELIVERED,
                "Cargo delivered", "Your cargo was delivered. Review the delivery in your trip.", false);
    }

    @EventListener
    public void on(IncidentReportedEvent event) {
        notifyShipper(event.shipperId(), NotificationType.INCIDENT_REPORTED,
                "Trip delayed by an incident", "The carrier reported an incident. Review the details in your trip.", true);
    }

    private void notifyShipper(UUID shipperId, NotificationType type, String title, String message, boolean urgent) {
        profilesContextFacade.fetchUserIdByShipperId(shipperId).ifPresent(userId ->
                notificationCommandService.handle(new CreateNotificationCommand(userId, type, title, message, urgent)));
    }
}
