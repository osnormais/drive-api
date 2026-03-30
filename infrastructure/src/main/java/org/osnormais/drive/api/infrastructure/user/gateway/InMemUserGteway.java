package org.osnormais.drive.api.infrastructure.user.gateway;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.osnormais.drive.api.application.gateway.user.UserCommandGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;
import org.springframework.stereotype.Component;

@Component
public class InMemUserGteway implements UserCommandGateway, UserQueryGateway {

    private static final ConcurrentHashMap<UserId, User> datasource = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findById(UserId id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public User create(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public User update(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
