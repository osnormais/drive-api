package org.osnormais.drive.api.application.usecase.folder.create;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.exception.DomainException;
import org.osnormais.drive.api.domain.exception.InconsistentStateException;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.folder.service.FolderCreationService;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final UserQueryGateway userQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FolderCommandGateway folderCommandGateway;
    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;

    private final DomainEventDispatcher eventDispatcher;

    public DefaultCreateFolderUseCase(
            final UserQueryGateway userQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final FolderCommandGateway folderCommandGateway,
            final AclQueryGateway aclQueryGateway,
            final AclCommandGateway aclCommandGateway,
            final DomainEventDispatcher eventDispatcher) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.folderCommandGateway = requireNonNull(folderCommandGateway);
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.eventDispatcher = requireNonNull(eventDispatcher);
    }

    @Transactional
    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {

        final UserId creatorId = UserId.of(input.creatorId());
        final FolderId parentFolderId = FolderId.of(input.parentFolderId());
        final FolderName name = FolderName.of(input.name());

        final User user = userQueryGateway
                .findById(creatorId)
                .orElseThrow(() -> NotFoundException.create(User.class, creatorId));

        final Folder parentFolder = folderQueryGateway
                .findVisibleById(parentFolderId, creatorId)
                .orElseThrow(() -> NotFoundException.create(Folder.class, parentFolderId));

        final Acl parentFolderAcl = aclQueryGateway
                .findByResource(AclResource.of(parentFolder))
                .orElseThrow(() -> InconsistentStateException.create(
                        Acl.class,
                        List.of(DomainException.Error
                                .with("ACL not found for parent folder [%s]".formatted(parentFolderId)))));

        parentFolderAcl.requiredPermission(creatorId, Permission.WRITE);

        final Boolean hasSiblingsWithSameName = folderQueryGateway.existsByParentIdAndName(parentFolderId, name);

        final Folder folder = FolderCreationService.createFolder(
                hasSiblingsWithSameName,
                user,
                parentFolder,
                name);

        final Acl folderAcl = parentFolderAcl.deriveFor(AclResource.of(folder));

        eventDispatcher.dispatch(
                eventDispatcher.append(aclCommandGateway.create(folderAcl)),
                eventDispatcher.append(folderCommandGateway.create(folder)));

        return CreateFolderOutput.from(folder);

    }

}