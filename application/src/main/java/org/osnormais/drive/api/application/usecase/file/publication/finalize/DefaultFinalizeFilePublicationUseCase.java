package org.osnormais.drive.api.application.usecase.file.publication.finalize;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.file.FileCommandGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventDispatcher;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.file.valueobject.Publication;

public class DefaultFinalizeFilePublicationUseCase extends FinalizeFilePublicationUseCase {

    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;
    private final DomainEventDispatcher eventDispatcher;

    public DefaultFinalizeFilePublicationUseCase(
            FileQueryGateway fileQueryGateway,
            FileCommandGateway fileCommandGateway,
            DomainEventDispatcher eventDispatcher) {
        this.fileQueryGateway = fileQueryGateway;
        this.fileCommandGateway = fileCommandGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Transactional
    @Override
    public void execute(final FinalizeFilePublicationInput input) {

        final FileId fileId = FileId.of(input.fileId());

        final File file = fileQueryGateway
                .findById(fileId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        if (Publication.Status.SUCCESS.equals(input.status()))
            file.successPublication();
        else
            file.failPublication(input.errorMessage().orElse(null));

        fileCommandGateway.update(file);

        eventDispatcher.dispatch(eventDispatcher.append(file));

    }

}
