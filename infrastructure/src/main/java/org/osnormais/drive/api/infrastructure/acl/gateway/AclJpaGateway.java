package org.osnormais.drive.api.infrastructure.acl.gateway;

import java.util.Optional;
import java.util.stream.Collectors;

import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclEntryJpa;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclEntryJpaRepository;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclEntryType;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AclJpaGateway implements AclCommandGateway, AclQueryGateway {

    private final AclJpaRepository aclRepository;
    private final AclEntryJpaRepository aclEntryRepository;

    public AclJpaGateway(final AclJpaRepository aclRepository, final AclEntryJpaRepository aclEntryRepository) {
        this.aclRepository = aclRepository;
        this.aclEntryRepository = aclEntryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Acl> findByResource(final AclResource<?> id) {
        return aclRepository
                .findByResourceIdAndResourceType(id.resourceId().getValue(), id.resourceType())
                .map(this::toDomain);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public Acl create(final Acl acl) {

        if (aclRepository.existsById(acl.getId().getValue()))
            throw new IllegalStateException("ACL with id %s already exists".formatted(acl.getId().getStringValue()));

        save(acl);

        return acl;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public Acl update(final Acl acl) {

        if (!aclRepository.existsById(acl.getId().getValue()))
            throw new IllegalStateException("ACL with id %s does not exist".formatted(acl.getId().getStringValue()));

        save(acl);

        return acl;
    }

    private void save(final Acl acl) {

        final AclJpa aclJpa = AclJpa.fromDomain(acl);

        aclEntryRepository
                .saveAll(
                        acl.getDirectEntries()
                                .stream()
                                .map(entry -> AclEntryJpa.fromDomain(aclJpa, AclEntryType.DIRECT, entry))
                                .collect(Collectors.toList()));
        aclEntryRepository
                .saveAll(
                        acl.getInheritedEntries()
                                .stream()
                                .map(entry -> AclEntryJpa.fromDomain(aclJpa, AclEntryType.INHERITED, entry))
                                .collect(Collectors.toList()));

        aclRepository.save(aclJpa);

    }

    private Acl toDomain(final AclJpa aclJpa) {
        return aclJpa.toDomain(
                aclEntryRepository.findAllByAclIdAndType(aclJpa.getId(), AclEntryType.DIRECT)
                        .stream()
                        .map(AclEntryJpa::toDomain)
                        .collect(Collectors.toSet()),
                aclEntryRepository.findAllByAclIdAndType(aclJpa.getId(), AclEntryType.INHERITED)
                        .stream()
                        .map(AclEntryJpa::toDomain)
                        .collect(Collectors.toSet()));
    }

}
