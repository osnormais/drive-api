package org.osnormais.drive.api.application.gateway.entitlement.grant;

import java.util.Optional;

import org.osnormais.drive.api.domain.entitlement.grant.GrantId;
import org.osnormais.drive.api.domain.entitlement.grant.UserEntitlementGrant;

public interface UserEntitlementGrantQueryGateway {

    Optional<UserEntitlementGrant> findById(GrantId id);

}
