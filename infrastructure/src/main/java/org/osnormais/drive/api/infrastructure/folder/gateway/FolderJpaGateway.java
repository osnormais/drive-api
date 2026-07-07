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
import org.osnormais.drive.api.domain.pagination.Page;
import org.osnormais.drive.api.domain.pagination.SearchQuery;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.filter.FilterService;
import org.osnormais.drive.api.infrastructure.filter.adapter.QueryAdapter;
import org.osnormais.drive.api.infrastructure.folder.persistence.FolderJpa;
import org.osnormais.drive.api.infrastructure.folder.persistence.FolderJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.JoinType;

@Component
public class FolderJpaGateway implements FolderCommandGateway, FolderQueryGateway {

    private final FolderJpaRepository folderRepository;
    private final FilterService filterService;

    public FolderJpaGateway(
            final FolderJpaRepository folderRepository,
            final FilterService filterService) {
        this.folderRepository = folderRepository;
        this.filterService = filterService;
    }

    @Transactional(readOnly = true)
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
                .findOne(withId(idValue)
                        .and(isOwnedByUser(userIdValue).or(hasAccessByUser(userIdValue, now))))
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
        final UUID idValue = id.getValue();
        return folderRepository
                .findAll(withParentId(idValue).or(withVirtualFolderId(idValue)))
                .stream()
                .map(FolderJpa::toDomain)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Folder> searchVisible(final SearchQuery query, final UserId userId) {
        final var page = QueryAdapter.of(query.pagination());

        final Instant now = Instant.now();

        final Specification<FolderJpa> specification = hasAccessByUser(userId.getValue(), now)
                .and(notDeleted())
                .and(filterService.build(
                        FolderJpa.class,
                        query.filterMethod(),
                        query.filters()));

        final var pageResult = this.folderRepository.findAll(specification, page);

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.toList())
                .map(FolderJpa::toDomain);
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

    private static Specification<FolderJpa> withParentId(final UUID folderId) {
        return (root, query, cb) -> cb.and(cb.equal(root.get("parentFolderId"), folderId));
    }

    private static Specification<FolderJpa> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    private static Specification<FolderJpa> withVirtualFolderId(final UUID folderId) {
        return (root, query, cb) -> {

            final var sub = query.subquery(Integer.class);
            final var subRoot = sub.from(FolderJpa.class);
            final var sharings = subRoot.join("sharings");

            sub
                    .select(cb.literal(1))
                    .where(
                            cb.equal(subRoot.get("id"), root.get("id")),
                            cb.equal(sharings.get("virtualFolder"), folderId));

            return cb.exists(sub);
        };
    }

    private static Specification<FolderJpa> isOwnedByUser(final UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("ownerId"), userId);
    }

    private static Specification<FolderJpa> hasAccessByUser(final UUID userId, final Instant now) {
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
                            cb.equal(acl.get("resourceType"), AclResourceType.FOLDER),
                            cb.or(cb.equal(acl.get("resourceOwnerId"), userId),
                                    cb.and(cb.equal(entries.get("id").get("userId"), userId),
                                            cb.or(
                                                    cb.isFalse(entries.get("hasExpiration")),
                                                    cb.greaterThan(entries.get("expiresAt"), now)))));

            return cb.or(isFolderOwner, cb.exists(accessSubquery));

        };
    }

}
