package com.cargolinklabs.loadmatch.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

/**
 * Assembler that translates between the user aggregate and its persistence entity
 */
public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        var entity = new UserPersistenceEntity();
        entity.setId(user.getId().userId());
        entity.setEmail(user.getEmail().address());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setActive(user.isActive());
        entity.setLastAccessAt(user.getLastAccessAt());
        return entity;
    }

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        return User.reconstitute(
                new UserId(entity.getId()),
                new EmailAddress(entity.getEmail()),
                entity.getPassword(),
                entity.getRole(),
                entity.isActive(),
                entity.getLastAccessAt());
    }
}
