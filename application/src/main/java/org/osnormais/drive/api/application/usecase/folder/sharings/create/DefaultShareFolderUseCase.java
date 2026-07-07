package org.osnormais.drive.api.application.usecase.folder.sharings.create;

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
import org.osnormais.drive.api.domain.folder.FolderType;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultShareFolderUseCase extends ShareFolderUseCase {

    private final UserQueryGateway userQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FolderCommandGateway folderCommandGateway;
    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;
    private final DomainEventDispatcher eventDispatcher;

    public DefaultShareFolderUseCase(
            final UserQueryGateway userQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final FolderCommandGateway folderCommandGateway,
            final AclQueryGateway aclQueryGateway,
            final AclCommandGateway aclCommandGateway,
            final DomainEventDispatcher eventDispatcher) {
        this.userQueryGateway = userQueryGateway;
        this.folderQueryGateway = folderQueryGateway;
        this.folderCommandGateway = folderCommandGateway;
        this.aclQueryGateway = aclQueryGateway;
        this.aclCommandGateway = aclCommandGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Transactional
    @Override
    public void execute(final ShareFolderInput input) {

        final FolderId folderId = FolderId.of(input.folderId());
        final UserId sharedTo = UserId.of(input.sharedTo());
        final UserId sharedBy = UserId.of(input.sharedBy());

        if (!userQueryGateway.existsById(sharedBy))
            throw NotFoundException.create(User.class, sharedBy);

        if (!userQueryGateway.existsById(sharedTo))
            throw NotFoundException.create(User.class, sharedTo);

        final Folder folder = folderQueryGateway
                .findVisibleById(folderId, sharedBy)
                .orElseThrow(() -> NotFoundException.create(Folder.class, folderId));

        final Acl folderAcl = aclQueryGateway
                .findByResource(AclResource.of(folder))
                .orElseThrow(() -> InconsistentStateException.create(
                        Acl.class,
                        List.of(DomainException.Error
                                .with("ACL not found for folder [%s]".formatted(folderId)))));

        folderAcl
                .requiredPermission(sharedBy, Permission.MANAGE)
                .grantDirectEntry(sharedBy, sharedTo, input.permission(), input.expiresAt());

        final Folder inboxFolder = folderQueryGateway
                .findByOwnerAndType(sharedTo, FolderType.INBOX)
                .orElseGet(() -> createInboxFolder(sharedTo));

        folder.share(sharedTo, sharedBy, inboxFolder.getId());

        eventDispatcher.dispatch(
                eventDispatcher.append(aclCommandGateway.update(folderAcl)),
                eventDispatcher.append(folderCommandGateway.update(folder)));

    }

    private Folder createInboxFolder(final UserId ownerId) {
        final Folder inboxFolder = Folder.createInbox(ownerId);
        final Acl inboxFolderAcl = Acl.create(AclResource.of(inboxFolder));

        eventDispatcher.dispatch(
                eventDispatcher.append(aclCommandGateway.create(inboxFolderAcl)),
                eventDispatcher.append(folderCommandGateway.create(inboxFolder)));

        return inboxFolder;
    }

}
