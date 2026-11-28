package com.cargolinklabs.loadmatch.platform.documents.domain.model.events;

import java.util.UUID;

public record DocumentValidatedEvent(UUID documentId, UUID carrierId, String status, boolean allMandatoryApproved) {
}
