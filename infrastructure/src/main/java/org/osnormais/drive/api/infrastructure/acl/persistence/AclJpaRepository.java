package org.osnormais.drive.api.infrastructure.acl.persistence;

import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.domain.acl.AclResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AclJpaRepository extends JpaRepository<AclJpa, UUID> {

    Optional<AclJpa> findByResourceIdAndResourceType(String resourceId, AclResourceType resourceType);

}
