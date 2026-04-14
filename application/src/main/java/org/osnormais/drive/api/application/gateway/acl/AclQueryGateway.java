package org.osnormais.drive.api.application.gateway.acl;

import java.util.Optional;

import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.AclId;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;

public interface AclQueryGateway {

    Optional<Acl> findById(AclId id);

    Optional<Acl> findByResource(AclResource<?> id);

}