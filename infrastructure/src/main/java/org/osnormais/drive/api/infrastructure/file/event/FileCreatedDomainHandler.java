package org.osnormais.drive.api.infrastructure.file.event;

import java.util.UUID;

import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class FileCreatedDomainHandler extends DomainEventHandler<FileCreatedEvent> {

    private static final UUID UNIQUE_ID = UUID.randomUUID();

    protected FileCreatedDomainHandler(final FileQueryGateway fileQueryGateway) {
        super(UNIQUE_ID, FileCreatedEvent.eventKey());
    }

    @Override
    public void handle(final FileCreatedEvent event) {

        System.out.println("FileCreatedDomainHandler.handle: " + event.toString());

    }

}
