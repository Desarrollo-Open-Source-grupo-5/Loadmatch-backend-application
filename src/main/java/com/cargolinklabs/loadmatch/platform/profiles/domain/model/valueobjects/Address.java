package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

/**
 * Address value object
 *
 * @param street the street and number
 * @param district the district
 * @param province the province
 * @param department the department
 */
public record Address(String street, String district, String province, String department) {
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or blank");
        }
        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("District cannot be null or blank");
        }
        province = province == null || province.isBlank() ? "Lima" : province;
        department = department == null || department.isBlank() ? "Lima" : department;
    }
}
