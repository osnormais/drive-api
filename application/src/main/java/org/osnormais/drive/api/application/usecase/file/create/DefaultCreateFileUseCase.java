package org.osnormais.drive.api.application.usecase.file.create;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.acl.AclCommandGateway;
import org.osnormais.drive.api.application.gateway.acl.AclQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.Permission;
import org.osnormais.drive.api.domain.acl.valueobject.AclResource;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.exception.DomainException;
import org.osnormais.drive.api.domain.exception.InconsistentStateException;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.service.FileCreationService;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.user.valueobject.Quota;
import org.osnormais.drive.api.domain.validation.handler.Notification;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final UserQueryGateway userQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;
    private final AclQueryGateway aclQueryGateway;
    private final AclCommandGateway aclCommandGateway;

    private final DomainEventDispatcher eventDispatcher;

    public DefaultCreateFileUseCase(
            final UserQueryGateway userQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final FileQueryGateway fileQueryGateway,
            final FileCommandGateway fileCommandGateway,
            final AclQueryGateway aclQueryGateway,
            final AclCommandGateway aclCommandGateway,
            final DomainEventDispatcher eventDispatcher) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCommandGateway = requireNonNull(fileCommandGateway);
        this.aclQueryGateway = requireNonNull(aclQueryGateway);
        this.aclCommandGateway = requireNonNull(aclCommandGateway);
        this.eventDispatcher = requireNonNull(eventDispatcher);
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final UserId creatorId = UserId.of(input.creatorId());
        final FolderId parentFolderId = FolderId.of(input.parentFolderId());
        final FileName fileName = FileName.of(input.name());
        final Content content = Content.of(input.contentType());
        final Size size = Size.of(input.sizeInBytes());
        final Checksum checksum = Checksum.of(input.checksumAlgorithm(), input.checksumValue());

        final ValidationHandler handler = Notification.create();

        creatorId.validate(handler);
        parentFolderId.validate(handler);
        fileName.validate(handler);
        size.validate(handler);
        checksum.validate(handler);
        content.validate(handler);

        if (handler.hasErrors())
            throw ValidationException.with("Invalid input values", handler);

        final User creator = userQueryGateway
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

        final User owner = userQueryGateway
                .findById(parentFolder.getOwner())
                .orElseThrow(() -> NotFoundException.create(User.class, parentFolder.getOwner()));

        final Quota availableQuota = owner
                .getQuota()
                .remaining(Quota.of(fileQueryGateway.totalSizeByUserId(owner.getId()).bytes()));

        final Boolean hasSiblingsWithSameName = fileQueryGateway.existsByFolderIdAndName(parentFolderId, fileName);

        final File file = FileCreationService.createFile(
                hasSiblingsWithSameName,
                availableQuota,
                creator,
                owner,
                parentFolder,
                fileName,
                checksum,
                size,
                content);

        final Acl fileAcl = parentFolderAcl.deriveFor(AclResource.of(file));

        eventDispatcher.notify(aclCommandGateway.create(fileAcl));
        eventDispatcher.notify(fileCommandGateway.create(file));

        return CreateFileOutput.of(file);

    }

}
