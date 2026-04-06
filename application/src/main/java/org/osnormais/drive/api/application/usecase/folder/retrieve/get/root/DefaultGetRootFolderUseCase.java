package org.osnormais.drive.api.application.usecase.folder.retrieve.get.root;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderCommandGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final UserQueryGateway userQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FolderCommandGateway folderCommandGateway;
    private final AclCommandGateway aclCommandGateway;

    private final DomainEventDispatcher domainEventDispatcher;

    public DefaultGetRootFolderUseCase(
            final UserQueryGateway userQueryGateway,
            final FileQueryGateway fileQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final DomainEventDispatcher domainEventDispatcher,
            final FolderCommandGateway folderCommandGateway,
            final AclCommandGateway aclCommandGateway) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.folderCommandGateway = requireNonNull(folderCommandGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.domainEventDispatcher = requireNonNull(domainEventDispatcher);
    }

    @Transactional
    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final UserId ownerId = UserId.of(input.ownerId());

        final User owner = userQueryGateway
                .findById(ownerId)
                .orElseThrow(() -> NotFoundException.create(User.class, ownerId));

        final Folder rootFolder = folderQueryGateway
                .findRootByOwner(owner.getId())
                .orElseGet(() -> createRootFolder(owner.getId()));

        return GetRootFolderOutput.from(
                rootFolder,
                this.folderQueryGateway.findAllByParent(rootFolder.getId()),
                this.fileQueryGateway.findAllByFolder(rootFolder.getId()));
    }

    private Folder createRootFolder(final UserId ownerId) {
        final Folder rootFolder = Folder.createRoot(ownerId);
        final Acl rootFolderAcl = Acl.create(AclResource.of(rootFolder));

        domainEventDispatcher.dispatch(
                domainEventDispatcher.append(aclCommandGateway.create(rootFolderAcl)),
                domainEventDispatcher.append(folderCommandGateway.create(rootFolder)));

        return rootFolder;
    }

}
