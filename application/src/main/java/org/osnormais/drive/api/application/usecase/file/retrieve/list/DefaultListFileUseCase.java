package org.osnormais.drive.api.application.usecase.file.retrieve.list;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultListFileUseCase extends ListFileUseCase {

    private final FileQueryGateway fileQueryGateway;

    public DefaultListFileUseCase(final FileQueryGateway fileQueryGateway) {
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
    }

    @Override
    public ListFileOutput execute(final ListFileInput input) {

        final UserId actorId = UserId.of(input.actorId());

        return new ListFileOutput(fileQueryGateway
                .searchVisible(input.query(), actorId)
                .map(file -> ListFileOutput.Item.from(file, actorId)));

    }

}
