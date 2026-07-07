package org.osnormais.drive.api.infrastructure.acl.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclEntryJpaRepository extends JpaRepository<AclEntryJpa, AclEntryIdJpa> {

    List<AclEntryJpa> findAllByAclIdAndType(final UUID aclId, final AclEntryType type);

}
