package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReportIncidentResource(
        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "ROAD_BLOCKAGE|MECHANICAL_FAILURE|ACCIDENT", message = "{trip.incident.type.invalid}") String type,
        @Size(max = 500, message = "{trip.incident.description.too-long}") String description,
        @NotBlank(message = "{validation.not-blank}")
        @Size(max = 255, message = "{trip.incident.file-name.invalid}")
        @Pattern(regexp = "[^\\p{Cntrl}]+", message = "{trip.incident.file-name.invalid}") String fileName,
        @NotBlank(message = "{validation.not-blank}") String contentType,
        @NotBlank(message = "{validation.not-blank}") String fileBase64) {
}
