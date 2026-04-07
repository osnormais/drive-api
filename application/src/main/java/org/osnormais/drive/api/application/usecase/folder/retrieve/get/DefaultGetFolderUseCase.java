package org.osnormais.drive.api.application.usecase.folder.retrieve.get;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultGetFolderUseCase extends GetFolderUseCase {

    private final FolderQueryGateway folderQueryGateway;
    private final FileQueryGateway fileQueryGateway;

    public DefaultGetFolderUseCase(
            final FolderQueryGateway folderQueryGateway,
            final FileQueryGateway fileQueryGateway) {
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
    }

    @Transactional
    @Override
    public GetFolderOutput execute(final GetFolderInput input) {

        final FolderId folderId = FolderId.of(input.folderId());
        final UserId userId = UserId.of(input.userId());

        final Folder folder = folderQueryGateway
                .findVisibleById(folderId, userId)
                .orElseThrow(() -> NotFoundException.create(Folder.class, folderId));

        return GetFolderOutput.from(
                folder,
                this.folderQueryGateway.findAllByParent(folder.getId()),
                this.fileQueryGateway.findAllByFolder(folder.getId()));
    }

}
