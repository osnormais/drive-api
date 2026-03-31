package org.osnormais.drive.api.infrastructure.file.event;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.infrastructure.file.data.message.FileIntegrationMessage;
import org.springframework.stereotype.Component;

@Component
public class FileCreatedIntegrationHandlerTeste3 extends DomainEventHandler<FileCreatedEvent> {

    private static final UUID UNIQUE_ID = UUID.randomUUID();
    private final FileQueryGateway fileQueryGateway;

    protected FileCreatedIntegrationHandlerTeste3(final FileQueryGateway fileQueryGateway) {
        super(UNIQUE_ID, FileCreatedEvent.eventKey());
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
    }

    @Override
    public void handle(final FileCreatedEvent event) {

        final File file = fileQueryGateway.findById(event.getIdentifier()).orElseThrow();

        FileIntegrationMessage.of(file);

    }

}
