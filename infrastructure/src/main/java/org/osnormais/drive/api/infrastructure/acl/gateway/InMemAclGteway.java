package org.osnormais.drive.api.infrastructure.acl.gateway;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.AclId;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.springframework.stereotype.Component;

@Component
public class InMemAclGteway implements AclCommandGateway, AclQueryGateway {

    private static final ConcurrentHashMap<AclId, Acl> datasource = new ConcurrentHashMap<>();

    @Override
    public Optional<Acl> findByResource(AclResource<?> id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByResource'");
    }

    @Override
    public Acl create(Acl acl) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Acl update(Acl acl) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
