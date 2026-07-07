package org.osnormais.drive.api.application.gateway.user;

import java.util.Optional;

import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public interface UserQueryGateway {

    Boolean existsById(UserId id);

    Optional<User> findById(UserId id);

}