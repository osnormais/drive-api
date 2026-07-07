package org.osnormais.drive.api.application.gateway.acl;

import org.osnormais.drive.api.domain.acl.Acl;

public interface AclCommandGateway {

    Acl create(Acl acl);

    Acl update(Acl acl);

}
