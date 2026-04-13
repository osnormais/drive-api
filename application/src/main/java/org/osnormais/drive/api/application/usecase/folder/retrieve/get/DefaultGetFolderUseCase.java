package org.osnormais.drive.api.application.usecase.folder.retrieve.get;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultGetFolderUseCase extends GetFolderUseCase {

    private final UserQueryGateway userQueryGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final FileQueryGateway fileQueryGateway;

    public DefaultGetFolderUseCase(
            final UserQueryGateway userQueryGateway,
            final FolderQueryGateway folderQueryGateway,
            final FileQueryGateway fileQueryGateway) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
    }

    @Transactional
    @Override
    public GetFolderOutput execute(final GetFolderInput input) {

        final FolderId folderId = FolderId.of(input.folderId());
        final UserId userId = UserId.of(input.userId());

        if (!userQueryGateway.existsById(userId))
            throw NotFoundException.create(User.class, userId);

        final Folder folder = folderQueryGateway
                .findVisibleById(folderId, userId)
                .orElseThrow(() -> NotFoundException.create(Folder.class, folderId));

        return GetFolderOutput.from(
                userId,
                folder,
                this.folderQueryGateway.findAllByParent(folder.getId()),
                this.fileQueryGateway.findAllByFolder(folder.getId()));
    }

}
