package org.osnormais.drive.api.infrastructure.acl.gateway;

import java.util.Optional;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AclJpaGateway implements AclCommandGateway, AclQueryGateway {

    private final AclJpaRepository aclRepository;

    public AclJpaGateway(final AclJpaRepository aclRepository) {
        this.aclRepository = aclRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Acl> findByResource(final AclResource<?> id) {
        return aclRepository
                .findByResourceIdAndResourceType(id.resourceId().getStringValue(), id.resourceType())
                .map(AclJpa::toDomain);
    }

    @Transactional
    @Override
    public Acl create(final Acl acl) {

        if (aclRepository.existsById(acl.getId().getValue()))
            throw new IllegalStateException("ACL with id %s already exists".formatted(acl.getId().getStringValue()));

        save(acl);

        return acl;
    }

    @Transactional
    @Override
    public Acl update(final Acl acl) {

        if (!aclRepository.existsById(acl.getId().getValue()))
            throw new IllegalStateException("ACL with id %s does not exist".formatted(acl.getId().getStringValue()));

        save(acl);

        return acl;
    }

    private void save(final Acl acl) {
        aclRepository.save(AclJpa.fromDomain(acl));
    }

}