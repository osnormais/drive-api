package org.osnormais.drive.api.infrastructure.folder.gateway;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.acl.AclResourceType;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.folder.persistence.FolderJpa;
import org.osnormais.drive.api.infrastructure.folder.persistence.FolderJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.JoinType;

@Component
public class FolderJpaGteway implements FolderCommandGateway, FolderQueryGateway {

    private final FolderJpaRepository folderRepository;

    public FolderJpaGteway(final FolderJpaRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public Optional<Folder> findByOwnerAndType(final UserId ownerId, final FolderType type) {
        return folderRepository
                .findByOwnerIdAndType(ownerId.getValue(), type)
                .map(FolderJpa::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Folder> findVisibleById(final FolderId id, final UserId userId) {

        final UUID idValue = id.getValue();
        final UUID userIdValue = userId.getValue();
        final Instant now = Instant.now();

        return folderRepository
                .findOne(withId(idValue).and(isOwnedByUser(userIdValue).or(hasAccessByUser(userIdValue, now))))
                .map(FolderJpa::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsByParentIdAndName(final FolderId parentFolderId, final FolderName name) {
        return folderRepository.existsByParentFolderIdAndName(parentFolderId.getValue(), name.value());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Folder> findAllByParent(final FolderId id) {
        return folderRepository.findAllByParentFolderId(id.getValue())
                .stream()
                .map(FolderJpa::toDomain)
                .collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public Folder create(final Folder folder) {
        if (folderRepository.existsById(folder.getId().getValue()))
            throw new IllegalStateException(
                    "Folder with id %s already exists".formatted(folder.getId().getStringValue()));

        save(folder);

        return folder;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public Folder update(final Folder folder) {
        if (!folderRepository.existsById(folder.getId().getValue()))
            throw new IllegalStateException(
                    "Folder with id %s does not exist".formatted(folder.getId().getStringValue()));

        save(folder);

        return folder;
    }

    private void save(final Folder folder) {
        folderRepository.save(FolderJpa.fromDomain(folder));
    }

    private static Specification<FolderJpa> withId(final UUID folderId) {
        return (root, query, cb) -> cb.and(cb.equal(root.get("id"), folderId));
    }

    private static Specification<FolderJpa> isOwnedByUser(final UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("ownerId"), userId);
    }

    private static Specification<FolderJpa> hasAccessByUser(final UUID userId, final Instant now) {
        return (root, query, cb) -> {

            if (isNull(query))
                return cb.conjunction();

            final var subQuery = query.subquery(UUID.class);
            final var aclRoot = subQuery.from(AclJpa.class);

            final var aclEntry = aclRoot.join("entries", JoinType.LEFT);

            final var isResourceAclMatched = cb.and(
                    cb.equal(aclRoot.get("resourceId"), root.get("id")),
                    cb.equal(aclRoot.get("resourceType"), AclResourceType.FOLDER));

            final var isOwner = cb.equal(aclRoot.get("resourceOwnerId"), userId);

            final var isEntryForUser = cb.equal(aclEntry.get("userId"), userId);
            final var entryNotExpired = cb.or(
                    cb.isFalse(aclEntry.get("hasExpiration")),
                    cb.greaterThan(aclEntry.get("expiresAt"), now));

            final var hasValidAclEntry = cb.and(
                    isEntryForUser,
                    entryNotExpired);

            subQuery
                    .select(aclRoot.get("resourceOwnerId"))
                    .where(cb.and(isResourceAclMatched, cb.or(isOwner, hasValidAclEntry)));

            return cb.exists(subQuery);
        };
    }

}
