package com.cargolinklabs.loadmatch.platform.documents.application.internal.eventhandlers;

import com.cargolinklabs.loadmatch.platform.documents.application.commandservices.DocumentCommandService;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.commands.SeedDocumentTypesCommand;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service("documentsApplicationReadyEventHandler")
public class ApplicationReadyEventHandler {
    private final DocumentCommandService commands;

    public ApplicationReadyEventHandler(DocumentCommandService commands) {
        this.commands = commands;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        commands.handle(new SeedDocumentTypesCommand());
    }
}
