package org.osnormais.drive.api.infrastructure.file.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FileJpaRepository extends JpaRepository<FileJpa, UUID>, JpaSpecificationExecutor<FileJpa> {

    Boolean existsByNameAndFolderId(String name, UUID folderId);

    @Query("""
                select
                    coalesce(sum(f.sizeInBytes), 0)
                from File f
                where f.ownerId = :ownerId
                and f.deletedAt is null
            """)
    Long sumSizeInBytesByOwnerId(UUID ownerId);

}
