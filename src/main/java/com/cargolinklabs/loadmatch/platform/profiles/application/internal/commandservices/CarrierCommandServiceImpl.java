package com.cargolinklabs.loadmatch.platform.profiles.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.profiles.application.commandservices.CarrierCommandService;
import com.cargolinklabs.loadmatch.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.EnableCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.RegisterCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.UpdateCarrierProfileCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Dni;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.DriverLicense;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.PhoneNumber;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.CarrierRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carrier command service implementation
 */
@Service
public class CarrierCommandServiceImpl implements CarrierCommandService {
    private final CarrierRepository carrierRepository;
    private final ExternalIamService externalIamService;

    public CarrierCommandServiceImpl(CarrierRepository carrierRepository, ExternalIamService externalIamService) {
        this.carrierRepository = carrierRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Result<Carrier, ApplicationError> handle(RegisterCarrierCommand command) {
        var dni = new Dni(command.dni());
        if (carrierRepository.existsByDni(dni)) {
            return Result.failure(ApplicationError.conflict(
                    "Carrier", "DNI %s is already registered".formatted(dni.dni())));
        }
        var license = new DriverLicense(command.licenseNumber(), command.licenseCategory(),
                command.licenseExpirationDate());
        if (!license.isValid()) {
            return Result.failure(ApplicationError.validationError(
                    "licenseExpirationDate", "The driver license has expired"));
        }
        var phoneNumber = new PhoneNumber(command.phoneNumber());
        return externalIamService.createUser(command.email(), command.password(), "CARRIER")
                .map(userId -> carrierRepository.save(new Carrier(userId, command.firstNames(),
                        command.lastNames(), dni, license, phoneNumber, command.operatingZone())));
    }

    @Override
    public Result<Carrier, ApplicationError> handle(EnableCarrierCommand command) {
        var carrier = carrierRepository.findById(command.carrierId());
        if (carrier.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Carrier", command.carrierId().carrierId().toString()));
        }
        carrier.get().enable();
        return Result.success(carrierRepository.save(carrier.get()));
    }

    @Override
    public Result<Carrier, ApplicationError> handle(UpdateCarrierProfileCommand command) {
        var carrier = carrierRepository.findById(command.carrierId());
        if (carrier.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Carrier", command.carrierId().carrierId().toString()));
        }
        carrier.get().updateProfile(new PhoneNumber(command.phoneNumber()), command.operatingZone(), command.photoUrl());
        return Result.success(carrierRepository.save(carrier.get()));
    }
}
