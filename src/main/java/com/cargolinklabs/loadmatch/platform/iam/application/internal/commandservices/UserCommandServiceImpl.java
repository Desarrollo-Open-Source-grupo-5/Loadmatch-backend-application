package com.cargolinklabs.loadmatch.platform.iam.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.iam.application.commandservices.UserCommandService;
import com.cargolinklabs.loadmatch.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.application.commandservices.AuthenticatedUser;
import com.cargolinklabs.loadmatch.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.commands.SignInCommand;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.commands.SignUpCommand;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.Roles;
import com.cargolinklabs.loadmatch.platform.iam.domain.repositories.UserRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * User command service implementation
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService,
                                  TokenService tokenService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    @Override
    public Result<User, ApplicationError> handle(SignUpCommand command) {
        var email = new EmailAddress(command.email());
        if (userRepository.existsByEmail(email)) {
            return Result.failure(ApplicationError.conflict(
                    "User", "Email %s is already registered".formatted(email.address())));
        }
        var user = new User(email, hashingService.encode(command.password()), Roles.fromName(command.role()));
        user.onRegistered();
        return Result.success(userRepository.save(user));
    }

    @Override
    public Result<AuthenticatedUser, ApplicationError> handle(SignInCommand command) {
        var user = userRepository.findByEmail(new EmailAddress(command.email()));
        if (user.isEmpty() || !hashingService.matches(command.password(), user.get().getPassword())) {
            return Result.failure(ApplicationError.validationError(
                    "credentials", "Invalid email or password"));
        }
        if (!user.get().isEnabled()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "active-account", "The account is deactivated"));
        }
        user.get().recordAccess();
        var savedUser = userRepository.save(user.get());
        var token = tokenService.generateToken(savedUser.getEmail().address());
        return Result.success(new AuthenticatedUser(savedUser, token));
    }
}
