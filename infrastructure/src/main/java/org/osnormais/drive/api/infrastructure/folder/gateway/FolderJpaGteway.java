package org.osnormais.drive.api.infrastructure.folder.gateway;

import static java.util.Objects.isNull;

import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.infrastructure.acl.persistence.AclJpa;
import org.osnormais.drive.api.infrastructure.folder.persistence.FolderJpa;
import org.osnormais.drive.api.infrastructure.folder.persistence.FolderJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FolderJpaGteway implements FolderCommandGateway, FolderQueryGateway {

    private final FolderJpaRepository folderRepository;

    public FolderJpaGteway(final FolderJpaRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Folder> findVisibleById(final FolderId id, final UserId userId) {

        final UUID idValue = id.getValue();
        final UUID userIdValue = userId.getValue();

        return folderRepository
                .findOne(withId(idValue).and(isOwnedByUser(userIdValue).or(hasAccessByUser(userIdValue))))
                .map(FolderJpa::toDomain);
    }

    @Transactional
    @Override
    public Folder create(final Folder folder) {
        if (folderRepository.existsById(folder.getId().getValue()))
            throw new IllegalStateException(
                    "Folder with id %s already exists".formatted(folder.getId().getStringValue()));

        save(folder);

        return folder;
    }

    @Transactional
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

    private static Specification<FolderJpa> hasAccessByUser(final UUID userId) {
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
