package com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects;

public record LicensePlateRef(String plate) {
    public LicensePlateRef {
        if (plate != null) {
            plate = plate.trim().toUpperCase().replace(" ", "");
        }
    }
}
