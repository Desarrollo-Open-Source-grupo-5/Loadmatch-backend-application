package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources;

import java.util.UUID;

public record DocumentTypeResource(UUID id, String code, String name, boolean mandatory, boolean requiresExpiration) {
}
