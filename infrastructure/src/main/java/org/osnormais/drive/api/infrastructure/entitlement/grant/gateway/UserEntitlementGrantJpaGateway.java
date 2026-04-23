package org.osnormais.drive.api.infrastructure.entitlement.grant.gateway;

import java.util.Optional;

import org.osnormais.drive.api.application.gateway.entitlement.grant.UserEntitlementGrantQueryGateway;
import org.osnormais.drive.api.domain.entitlement.grant.GrantId;
import org.osnormais.drive.api.domain.entitlement.grant.UserEntitlementGrant;
import org.springframework.stereotype.Component;

@Component
public class UserEntitlementGrantJpaGateway implements UserEntitlementGrantQueryGateway {

    @Override
    public Optional<UserEntitlementGrant> findById(final GrantId id) {
        return Optional.empty();
    }

}
