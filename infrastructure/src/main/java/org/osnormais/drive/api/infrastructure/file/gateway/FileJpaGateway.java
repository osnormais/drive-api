package org.osnormais.drive.api.infrastructure.file.gateway;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.acl.AclResourceType;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.SearchQuery;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.file.persistence.FileJpa;
import org.osnormais.drive.api.infrastructure.file.persistence.FileJpaRepository;
import org.osnormais.drive.api.infrastructure.filter.FilterService;
import org.osnormais.drive.api.infrastructure.filter.adapter.QueryAdapter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.JoinType;

@Component
public class FileJpaGateway implements FileCommandGateway, FileQueryGateway {

    private final FileJpaRepository fileRepository;
    private final FilterService filterService;

    public FileJpaGateway(
            final FileJpaRepository fileJpaRepository,
            final FilterService filterService) {
        this.fileRepository = fileJpaRepository;
        this.filterService = filterService;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<File> findById(final FileId id) {
        return fileRepository
                .findById(id.getValue())
                .map(FileJpa::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<File> findVisibleById(FileId id, UserId userId) {

        final UUID idValue = id.getValue();
        final UUID userIdValue = userId.getValue();
        final Instant now = Instant.now();

        return fileRepository
                .findOne(withId(idValue)
                        .and(isOwnedByUser(userIdValue).or(hasAccessByUser(userIdValue, now)))
                        .and(notDeleted()))
                .map(FileJpa::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public FileSize totalSizeByUserId(final UserId userId) {
        return FileSize.of(fileRepository.sumSizeInBytesByOwnerId(userId.getValue()));
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsByFolderIdAndName(final FolderId parentFolderId, final FileName fileName) {
        return fileRepository.existsByNameAndFolderId(fileName.value(), parentFolderId.getValue());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<File> findAllByFolder(FolderId id) {
        return fileRepository
                .findAll(withFolderId(id.getValue())
                        .or(withVirtualFolderId(id.getValue()))
                        .and(notDeleted()))
                .stream()
                .map(FileJpa::toDomain)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<File> searchVisible(final SearchQuery query, final UserId userId) {

        final var page = QueryAdapter.of(query.pagination());

        final Instant now = Instant.now();

        final Specification<FileJpa> specification = hasAccessByUser(userId.getValue(), now)
                .and(notDeleted())
                .and(filterService.build(
                        FileJpa.class,
                        query.filterMethod(),
                        query.filters()));

        final var pageResult = this.fileRepository.findAll(specification, page);

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.toList())
                .map(FileJpa::toDomain);

    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public File create(final File file) {

        if (fileRepository.existsById(file.getId().getValue()))
            throw new IllegalStateException("File with id %s already exists".formatted(file.getId().getStringValue()));

        save(file);

        return file;
    }

    @Transactional(propagation = Propagation.MANDATORY)
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

    private static Specification<FileJpa> withFolderId(final UUID folderId) {
        return (root, query, cb) -> cb.equal(root.get("folderId"), folderId);
    }

    private static Specification<FileJpa> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    private static Specification<FileJpa> withVirtualFolderId(final UUID folderId) {
        return (root, query, cb) -> {

            final var sub = query.subquery(Integer.class);
            final var subRoot = sub.from(FileJpa.class);
            final var sharings = subRoot.join("sharings");

            sub.select(cb.literal(1))
                    .where(
                            cb.equal(subRoot.get("id"), root.get("id")),
                            cb.equal(sharings.get("virtualFolder"), folderId));

            return cb.exists(sub);
        };
    }

    private static Specification<FileJpa> isOwnedByUser(final UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("ownerId"), userId);
    }

    private static Specification<FileJpa> hasAccessByUser(final UUID userId, final Instant now) {
        return (root, query, cb) -> {

            if (isNull(query))
                return cb.conjunction();

            var isFolderOwner = cb.equal(root.get("ownerId"), userId);

            var accessSubquery = query.subquery(Integer.class);
            var acl = accessSubquery.from(AclJpa.class);

            var entries = acl.join("entries", JoinType.LEFT);

            accessSubquery.select(cb.literal(1))
                    .where(
                            cb.equal(acl.get("resourceId"), root.get("id")),
                            cb.equal(acl.get("resourceType"), AclResourceType.FILE),
                            cb.or(
                                    cb.equal(acl.get("resourceOwnerId"), userId),
                                    cb.and(cb.equal(entries.get("id").get("userId"), userId),
                                            cb.or(
                                                    cb.isFalse(entries.get("hasExpiration")),
                                                    cb.greaterThan(entries.get("expiresAt"), now)))));

            return cb.or(isFolderOwner, cb.exists(accessSubquery));
        };
    }

}
