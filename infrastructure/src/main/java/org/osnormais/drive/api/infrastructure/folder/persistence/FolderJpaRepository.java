package org.osnormais.drive.api.infrastructure.folder.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FolderJpaRepository extends JpaRepository<FolderJpa, UUID>, JpaSpecificationExecutor<FolderJpa> {

}
