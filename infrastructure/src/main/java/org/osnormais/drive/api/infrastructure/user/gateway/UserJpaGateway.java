package org.osnormais.drive.api.infrastructure.user.gateway;

import java.util.Optional;

import org.osnormais.drive.api.application.gateway.user.UserCommandGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.user.persistence.UserJpa;
import org.osnormais.drive.api.infrastructure.user.persistence.UserJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserJpaGateway implements UserCommandGateway, UserQueryGateway {

    private final UserJpaRepository userRepository;

    public UserJpaGateway(final UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsById(final UserId id) {
        return userRepository.existsById(id.getValue());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(final UserId id) {
        return userRepository
                .findById(id.getValue())
                .map(UserJpa::toDomain);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public User create(final User user) {
        if (userRepository.existsById(user.getId().getValue()))
            throw new IllegalStateException("User with id %s already exists".formatted(user.getId().getStringValue()));

        save(user);

        return user;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public User update(final User user) {
        if (!userRepository.existsById(user.getId().getValue()))
            throw new IllegalStateException("User with id %s does not exist".formatted(user.getId().getStringValue()));

        save(user);

        return user;
    }

    private void save(final User user) {
        userRepository.save(UserJpa.fromDomain(user));
    }

}
