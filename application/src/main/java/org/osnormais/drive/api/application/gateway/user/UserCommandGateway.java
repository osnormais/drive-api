package org.osnormais.drive.api.application.gateway.user;

import org.osnormais.drive.api.domain.user.User;

public interface UserCommandGateway {

    User create(User user);

    User update(User user);

}
