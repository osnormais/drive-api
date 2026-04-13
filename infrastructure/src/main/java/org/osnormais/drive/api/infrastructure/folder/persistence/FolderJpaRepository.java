package org.osnormais.drive.api.infrastructure.folder.persistence;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.FolderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FolderJpaRepository extends JpaRepository<FolderJpa, UUID>, JpaSpecificationExecutor<FolderJpa> {

    Optional<FolderJpa> findByOwnerIdAndType(UUID ownerId, FolderType type);

    Boolean existsByParentFolderIdAndName(UUID parentFolderId, String name);

    Set<FolderJpa> findAllByParentFolderId(UUID parentFolderId);

}
