package com.cargolinklabs.loadmatch.platform.profiles.application.internal.eventhandlers;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.events.DocumentValidatedEvent;
import com.cargolinklabs.loadmatch.platform.profiles.application.commandservices.CarrierCommandService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.EnableCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class DocumentValidatedEventHandler {
    private final CarrierCommandService carrierCommandService;

    public DocumentValidatedEventHandler(CarrierCommandService carrierCommandService) {
        this.carrierCommandService = carrierCommandService;
    }

    @EventListener
    public void on(DocumentValidatedEvent event) {
        if (event.allMandatoryApproved()) {
            carrierCommandService.handle(new EnableCarrierCommand(new CarrierId(event.carrierId())));
        }
    }
}
