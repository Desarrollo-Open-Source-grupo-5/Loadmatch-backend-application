package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.events.DocumentValidatedEvent;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@RecordApplicationEvents
class DocumentsControllerIntegrationTest {
    private static final String DOCUMENTS = "/api/v1/documents";
    private static final String DOCUMENT_TYPES = "/api/v1/document-types";
    private static final String PASSWORD = "TestPassword123!";
    private static final byte[] PDF = "%PDF-1.7\n%%EOF".getBytes(StandardCharsets.US_ASCII);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter securityFilter;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = webAppContextSetup(context).addFilters(securityFilter).build();
    }

    @Test
    void validatesDocumentsPersistsFilesAndPublishesReadiness(ApplicationEvents events) throws Exception {
        var owner = registerCarrier();
        var other = registerCarrier();
        mvc.perform(get(DOCUMENT_TYPES)).andExpect(status().isUnauthorized());

        var plate = registerVehicle(owner);
        var expiration = LocalDate.now().plusYears(1).toString();
        var license = upload(owner, "DRIVER_LICENSE", null, expiration);
        var insurance = upload(owner, "SOAT", plate, expiration);
        var registration = upload(owner, "VEHICLE_REGISTRATION", plate, null);

        assertThat(events.stream(DocumentValidatedEvent.class)
                .filter(event -> event.carrierId().toString().equals(owner.id()))
                .map(DocumentValidatedEvent::allMandatoryApproved))
                .containsExactly(false, false, true);
        var fileUrl = read(registration, "$.fileUrl");

        mvc.perform(get(DOCUMENTS).param("carrierId", owner.id()).header(HttpHeaders.AUTHORIZATION, owner.bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(read(registration, "$.id")))
                .andExpect(jsonPath("$[1].id").value(read(insurance, "$.id")))
                .andExpect(jsonPath("$[2].id").value(read(license, "$.id")));
        mvc.perform(get(fileUrl).header(HttpHeaders.AUTHORIZATION, owner.bearer()))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_DISPOSITION))
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "no-store"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(PDF));
        mvc.perform(get(fileUrl).header(HttpHeaders.AUTHORIZATION, other.bearer()))
                .andExpect(status().isNotFound());
        mvc.perform(get(DOCUMENTS).param("carrierId", owner.id()).header(HttpHeaders.AUTHORIZATION, other.bearer()))
                .andExpect(status().isNotFound());
        mvc.perform(post(DOCUMENTS).header(HttpHeaders.AUTHORIZATION, other.bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(owner.id(), documentType(owner, "DRIVER_LICENSE"), null, expiration, "badBase64")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsAnExpiredDocumentWrongPlateAndInvalidFiles() throws Exception {
        var carrier = registerCarrier();
        var licenseType = documentType(carrier, "DRIVER_LICENSE");
        var soatType = documentType(carrier, "SOAT");
        var encodedPdf = Base64.getEncoder().encodeToString(PDF);
        var future = LocalDate.now().plusYears(1).toString();
        var expired = LocalDate.now().minusDays(2).toString();

        mvc.perform(post(DOCUMENTS).header(HttpHeaders.AUTHORIZATION, carrier.bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(carrier.id(), licenseType, null, expired, encodedPdf)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.validationStatus").value("REJECTED"))
                .andExpect(jsonPath("$.rejectionReasonCode").value("DOCUMENT_EXPIRED"));
        mvc.perform(post(DOCUMENTS).header(HttpHeaders.AUTHORIZATION, carrier.bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(carrier.id(), soatType, "ABC-123", future, encodedPdf)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rejectionReasonCode").value("PLATE_MISMATCH"));
        mvc.perform(post(DOCUMENTS).header(HttpHeaders.AUTHORIZATION, carrier.bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(carrier.id(), licenseType, null, future, "YQ==")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        mvc.perform(get(DOCUMENTS).param("carrierId", carrier.id()).header(HttpHeaders.AUTHORIZATION, carrier.bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private MvcResult upload(CarrierSession carrier, String code, String plate, String expiration) throws Exception {
        return mvc.perform(post(DOCUMENTS).header(HttpHeaders.AUTHORIZATION, carrier.bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload(carrier.id(), documentType(carrier, code), plate, expiration,
                                Base64.getEncoder().encodeToString(PDF))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.validationStatus").value("APPROVED"))
                .andExpect(jsonPath("$.fileName").value("test-document.pdf"))
                .andReturn();
    }

    private String documentType(CarrierSession carrier, String code) throws Exception {
        var result = mvc.perform(get(DOCUMENT_TYPES).header(HttpHeaders.AUTHORIZATION, carrier.bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
        List<String> ids = JsonPath.read(result.getResponse().getContentAsString(), "$[?(@.code == '%s')].id".formatted(code));
        return ids.getFirst();
    }

    private String registerVehicle(CarrierSession carrier) throws Exception {
        var types = mvc.perform(get("/api/v1/vehicle-types")).andExpect(status().isOk()).andReturn();
        var plate = UUID.randomUUID().toString().substring(0, 3) + "-"
                + ThreadLocalRandom.current().nextInt(100, 1000);
        mvc.perform(post("/api/v1/vehicles").header(HttpHeaders.AUTHORIZATION, carrier.bearer())
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                  "carrierId": "%s", "vehicleTypeId": "%s", "licensePlate": "%s",
                                  "brand": "Test", "model": "Truck", "manufactureYear": 2020, "payloadKg": 500
                                }
                                """.formatted(carrier.id(), read(types, "$[0].id"), plate)))
                .andExpect(status().isCreated());
        return plate;
    }

    private CarrierSession registerCarrier() throws Exception {
        var email = "t66-" + UUID.randomUUID() + "@example.invalid";
        var dni = ThreadLocalRandom.current().nextInt(10_000_000, 100_000_000);
        var carrier = mvc.perform(post("/api/v1/carriers").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "email": "%s", "password": "%s", "firstNames": "Document", "lastNames": "Test",
                          "dni": "%s", "licenseNumber": "TEST-%s", "licenseCategory": "A-IIb",
                          "licenseExpirationDate": "%s", "phoneNumber": "999123456", "operatingZone": "Lima"
                        }
                        """.formatted(email, PASSWORD, dni, dni, LocalDate.now().plusYears(1))))
                .andExpect(status().isCreated()).andReturn();
        var token = mvc.perform(post("/api/v1/tokens").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "%s"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isOk()).andReturn();
        return new CarrierSession(read(carrier, "$.id"), "Bearer " + read(token, "$.token"));
    }

    private static String payload(String carrierId, String type, String plate, String expiration, String encoded) {
        return """
                {
                  "carrierId": "%s", "documentTypeId": "%s", "documentNumber": "T66-0001",
                  "licensePlate": %s, "expirationDate": %s, "fileName": "test-document.pdf",
                  "contentType": "application/pdf", "fileBase64": "%s"
                }
                """.formatted(carrierId, type, jsonStringOrNull(plate), jsonStringOrNull(expiration), encoded);
    }

    private static String jsonStringOrNull(String value) {
        return value == null ? "null" : "\"" + value + "\"";
    }

    private static String read(MvcResult result, String path) throws Exception {
        return JsonPath.read(result.getResponse().getContentAsString(), path);
    }

    private record CarrierSession(String id, String bearer) {
    }
}
