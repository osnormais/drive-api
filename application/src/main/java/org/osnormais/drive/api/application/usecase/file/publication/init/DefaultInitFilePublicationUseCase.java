package org.osnormais.drive.api.application.usecase.file.publication.init;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultInitFilePublicationUseCase extends InitFilePublicationUseCase {

    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;
    private final DomainEventDispatcher eventDispatcher;

    public DefaultInitFilePublicationUseCase(
            FileQueryGateway fileQueryGateway,
            FileCommandGateway fileCommandGateway,
            DomainEventDispatcher eventDispatcher) {
        this.fileQueryGateway = fileQueryGateway;
        this.fileCommandGateway = fileCommandGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Transactional
    @Override
    public void execute(final InitFilePublicationInput input) {

        final UserId actorId = UserId.of(input.actorId());
        final FileId fileId = FileId.of(input.fileId());

        final File file = fileQueryGateway
                .findVisibleById(fileId, actorId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        file.initPublication(actorId);

        eventDispatcher.dispatch(eventDispatcher.append(fileCommandGateway.update(file)));

    }

}
