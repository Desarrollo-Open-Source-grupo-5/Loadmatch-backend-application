package com.cargolinklabs.loadmatch.platform.profiles.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.profiles.application.commandservices.ShipperCommandService;
import com.cargolinklabs.loadmatch.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.RegisterShipperCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Address;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.PhoneNumber;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Ruc;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.ShipperRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Shipper command service implementation
 */
@Service
public class ShipperCommandServiceImpl implements ShipperCommandService {
    private final ShipperRepository shipperRepository;
    private final ExternalIamService externalIamService;

    public ShipperCommandServiceImpl(ShipperRepository shipperRepository, ExternalIamService externalIamService) {
        this.shipperRepository = shipperRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Result<Shipper, ApplicationError> handle(RegisterShipperCommand command) {
        var ruc = new Ruc(command.ruc());
        if (shipperRepository.existsByRuc(ruc)) {
            return Result.failure(ApplicationError.conflict(
                    "Shipper", "RUC %s is already registered".formatted(ruc.ruc())));
        }
        var phoneNumber = new PhoneNumber(command.phoneNumber());
        var fiscalAddress = new Address(command.street(), command.district(), command.province(), command.department());
        return externalIamService.createUser(command.email(), command.password(), "SHIPPER")
                .map(userId -> {
                    var shipper = new Shipper(userId, ruc, command.businessName(), command.contactName(),
                            phoneNumber, fiscalAddress);
                    shipper.onRegistered();
                    return shipperRepository.save(shipper);
                });
    }
}
