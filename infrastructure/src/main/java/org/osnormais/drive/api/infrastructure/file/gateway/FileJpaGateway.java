package org.osnormais.drive.api.infrastructure.file.gateway;

import static java.util.Objects.isNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.file.persistence.FileJpa;
import org.osnormais.drive.api.infrastructure.file.persistence.FileJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FileJpaGateway implements FileCommandGateway, FileQueryGateway {

    private final FileJpaRepository fileRepository;

    public FileJpaGateway(final FileJpaRepository fileJpaRepository) {
        this.fileRepository = fileJpaRepository;
    }

    @Override
    public Optional<File> findById(final FileId id) {
        return fileRepository
                .findById(id.getValue())
                .map(FileJpa::toDomain);
    }

    @Override
    public Optional<File> findVisibleById(FileId id, UserId userId) {
        final UUID idValue = id.getValue();
        final UUID userIdValue = userId.getValue();

        return fileRepository
                .findOne(withId(idValue).and(isOwnedByUser(userIdValue).or(hasAccessByUser(userIdValue))))
                .map(FileJpa::toDomain);
    }

    @Override
    public Size totalSizeByUserId(final UserId userId) {
        return Size.of(fileRepository.sumSizeInBytesByOwnerId(userId.getValue()));
    }

    @Override
    public Boolean existsByFolderIdAndName(final FolderId parentFolderId, final FileName fileName) {
        return fileRepository.existsByNameAndFolderId(fileName.value(), parentFolderId.getValue());
    }

    @Override
    public Set<File> findAllByFolder(FolderId id) {
        return fileRepository.findAllByFolderId(id.getValue())
                .stream()
                .map(FileJpa::toDomain)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public File create(final File file) {

        if (fileRepository.existsById(file.getId().getValue()))
            throw new IllegalStateException("File with id %s already exists".formatted(file.getId().getStringValue()));

        save(file);

        return file;
    }

    @Transactional
    @Override
    public File update(final File file) {
        if (!fileRepository.existsById(file.getId().getValue()))
            throw new IllegalStateException("File with id %s does not exist".formatted(file.getId().getStringValue()));

        save(file);

        return file;
    }

    private void save(final File file) {
        fileRepository.save(FileJpa.fromDomain(file));
    }

    private static Specification<FileJpa> withId(final UUID fileId) {
        return (root, query, cb) -> cb.and(cb.equal(root.get("id"), fileId));
    }

    private static Specification<FileJpa> isOwnedByUser(final UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("ownerId"), userId);
    }

    private static Specification<FileJpa> hasAccessByUser(final UUID userId) {
        return (root, query, cb) -> {

            if (isNull(query))
                return cb.conjunction();

            final var subQuery = query.subquery(String.class);
            final var aclRoot = subQuery.from(AclJpa.class);

            subQuery
                    .select(aclRoot.get("resourceOwnerId"))
                    .where(cb.and(
                            cb.equal(aclRoot.get("resourceOwnerId"), userId),
                            cb.equal(
                                    root.get("id"),
                                    aclRoot.get("resourceId").cast(UUID.class))));

            return cb.exists(subQuery);
        };
    }

}
