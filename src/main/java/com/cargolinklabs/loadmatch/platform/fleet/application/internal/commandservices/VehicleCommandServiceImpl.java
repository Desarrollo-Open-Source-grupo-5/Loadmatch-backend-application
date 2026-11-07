package com.cargolinklabs.loadmatch.platform.fleet.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.fleet.application.commandservices.VehicleCommandService;
import com.cargolinklabs.loadmatch.platform.fleet.application.internal.outboundservices.acl.ExternalProfilesService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.DeactivateVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.RegisterVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.LicensePlate;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleTypeId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleRepository;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleTypeRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Vehicle command service implementation
 */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {
    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final ExternalProfilesService externalProfilesService;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository, VehicleTypeRepository vehicleTypeRepository,
                                     ExternalProfilesService externalProfilesService) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.externalProfilesService = externalProfilesService;
    }

    @Override
    public Result<Vehicle, ApplicationError> handle(RegisterVehicleCommand command) {
        var carrierId = new CarrierId(command.carrierId());
        if (!externalProfilesService.existsCarrier(carrierId)) {
            return Result.failure(ApplicationError.notFound("Carrier", command.carrierId().toString()));
        }
        var licensePlate = new LicensePlate(command.licensePlate());
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            return Result.failure(ApplicationError.conflict(
                    "Vehicle", "License plate %s is already registered".formatted(licensePlate.licensePlate())));
        }
        var vehicleTypeId = new VehicleTypeId(command.vehicleTypeId());
        var typeValidation = validateVehicleType(vehicleTypeId, command.payloadKg());
        if (typeValidation != null) {
            return Result.failure(typeValidation);
        }
        var vehicle = new Vehicle(carrierId, vehicleTypeId, licensePlate, command.brand(), command.model(),
                command.manufactureYear(), command.payloadKg());
        vehicle.onRegistered();
        return Result.success(vehicleRepository.save(vehicle));
    }

    @Override
    public Result<Vehicle, ApplicationError> handle(UpdateVehicleCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId());
        if (vehicle.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Vehicle", command.vehicleId().vehicleId().toString()));
        }
        var vehicleTypeId = new VehicleTypeId(command.vehicleTypeId());
        var typeValidation = validateVehicleType(vehicleTypeId, command.payloadKg());
        if (typeValidation != null) {
            return Result.failure(typeValidation);
        }
        vehicle.get().updateTechnicalData(vehicleTypeId, command.brand(), command.model(),
                command.manufactureYear(), command.payloadKg());
        return Result.success(vehicleRepository.save(vehicle.get()));
    }

    @Override
    public Result<Vehicle, ApplicationError> handle(DeactivateVehicleCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId());
        if (vehicle.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Vehicle", command.vehicleId().vehicleId().toString()));
        }
        vehicle.get().deactivate();
        return Result.success(vehicleRepository.save(vehicle.get()));
    }

    private ApplicationError validateVehicleType(VehicleTypeId vehicleTypeId, double payloadKg) {
        var vehicleType = vehicleTypeRepository.findById(vehicleTypeId);
        if (vehicleType.isEmpty()) {
            return ApplicationError.notFound("Vehicle_Type", vehicleTypeId.vehicleTypeId().toString());
        }
        if (!vehicleType.get().acceptsLoad(payloadKg)) {
            return ApplicationError.businessRuleViolation("vehicle-type-capacity",
                    "A %s admits up to %.0f kg".formatted(vehicleType.get().getName(), vehicleType.get().getMaxWeightKg()));
        }
        return null;
    }
}
