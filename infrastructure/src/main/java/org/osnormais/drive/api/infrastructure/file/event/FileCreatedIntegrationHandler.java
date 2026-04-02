package org.osnormais.drive.api.infrastructure.file.event;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.infrastructure.file.data.message.FileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file.FileCreatedIntegrationProducer;
import org.springframework.stereotype.Component;

@Component
public class FileCreatedIntegrationHandler extends DomainEventHandler<FileCreatedEvent> {

    private static final UUID UNIQUE_ID = UUID.fromString("bedf635b-f8a2-4ccf-9eb6-5791a3ff95f7");
    private final FileQueryGateway fileQueryGateway;
    private final FileCreatedIntegrationProducer fileCreatedIntegrationProducer;

    protected FileCreatedIntegrationHandler(
            final FileQueryGateway fileQueryGateway,
            final FileCreatedIntegrationProducer fileCreatedIntegrationProducer) {
        super(UNIQUE_ID, FileCreatedEvent.eventKey());
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCreatedIntegrationProducer = requireNonNull(fileCreatedIntegrationProducer);
    }

    @Override
    public void handle(final FileCreatedEvent event) {

        final File file = fileQueryGateway.findById(event.getIdentifier()).orElseThrow();
        fileCreatedIntegrationProducer.produce(FileIntegrationMessage.of(file));

    }

}
