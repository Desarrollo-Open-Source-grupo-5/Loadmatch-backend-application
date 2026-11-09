package com.cargolinklabs.loadmatch.platform.iam.infrastructure.seeding.demo;

import com.cargolinklabs.loadmatch.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.Roles;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.iam.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Demo users seeder
 * <p>
 *     Creates the demo shippers and carriers the first time the platform starts, so the web application can sign in
 *     before the sign-up flow exists. It only runs when {@code loadmatch.seed.enabled=true} and it never duplicates a
 *     user: accounts whose email address already exists are skipped.
 * </p>
 * <p>
 *     The identifiers are fixed because the web application links each user with its shipper or carrier profile
 *     through the {@code userId} field of its simulated API (json-server).
 * </p>
 */
@Component
@ConditionalOnProperty(name = "loadmatch.seed.enabled", havingValue = "true")
public class DemoUsersSeeder implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoUsersSeeder.class);

    private record DemoUser(String id, String email, Roles role) {
    }

    private static final List<DemoUser> DEMO_USERS = List.of(
            new DemoUser("a1000000-0000-4000-8000-000000000001", "shipper1@loadmatch.demo", Roles.ROLE_SHIPPER),
            new DemoUser("a1000000-0000-4000-8000-000000000002", "shipper2@loadmatch.demo", Roles.ROLE_SHIPPER),
            new DemoUser("a1000000-0000-4000-8000-000000000003", "shipper3@loadmatch.demo", Roles.ROLE_SHIPPER),
            new DemoUser("a1000000-0000-4000-8000-000000000004", "shipper4@loadmatch.demo", Roles.ROLE_SHIPPER),
            new DemoUser("c1000000-0000-4000-8000-000000000001", "carrier1@loadmatch.demo", Roles.ROLE_CARRIER),
            new DemoUser("c1000000-0000-4000-8000-000000000002", "carrier2@loadmatch.demo", Roles.ROLE_CARRIER),
            new DemoUser("c1000000-0000-4000-8000-000000000003", "carrier3@loadmatch.demo", Roles.ROLE_CARRIER)
    );

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final String demoPassword;

    public DemoUsersSeeder(UserRepository userRepository,
                           HashingService hashingService,
                           @Value("${loadmatch.seed.password:Loadmatch123}") String demoPassword) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.demoPassword = demoPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        var created = 0;
        for (var demo : DEMO_USERS) {
            var email = new EmailAddress(demo.email());
            if (userRepository.existsByEmail(email)) {
                continue;
            }
            userRepository.save(User.reconstitute(
                    new UserId(UUID.fromString(demo.id())),
                    email,
                    hashingService.encode(demoPassword),
                    demo.role(),
                    true,
                    null));
            created++;
        }
        LOGGER.info("Demo users seeded: {} created, {} already existed", created, DEMO_USERS.size() - created);
    }
}
