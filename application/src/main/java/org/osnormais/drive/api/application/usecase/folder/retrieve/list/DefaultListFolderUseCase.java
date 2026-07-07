package org.osnormais.drive.api.application.usecase.folder.retrieve.list;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.folder.FolderQueryGateway;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultListFolderUseCase extends ListFolderUseCase {

    private final FolderQueryGateway folderQueryGateway;

    public DefaultListFolderUseCase(final FolderQueryGateway folderQueryGateway) {
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
    }

    @Override
    public ListFolderOutput execute(final ListFolderInput input) {

        final UserId actorId = UserId.of(input.actorId());

        return new ListFolderOutput(
                folderQueryGateway
                        .searchVisible(input.query(), actorId)
                        .map(folder -> ListFolderOutput.Item.from(folder, actorId)));

    }

}
