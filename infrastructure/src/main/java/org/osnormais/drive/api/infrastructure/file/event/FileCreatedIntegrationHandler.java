package org.osnormais.drive.api.infrastructure.file.event;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.infrastructure.file.data.message.FileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file.FileCreatedIntegrationProducer;
import org.springframework.stereotype.Component;

@Component
public class FileCreatedIntegrationHandler extends DomainEventHandler<FileCreatedEvent> {

    private final FileQueryGateway fileQueryGateway;
    private final FileCreatedIntegrationProducer fileCreatedIntegrationProducer;

    protected FileCreatedIntegrationHandler(
            final FileQueryGateway fileQueryGateway,
            final FileCreatedIntegrationProducer fileCreatedIntegrationProducer) {
        super(FileCreatedEvent.eventKey());
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCreatedIntegrationProducer = requireNonNull(fileCreatedIntegrationProducer);
    }

    @Override
    public void handle(final FileCreatedEvent event) {

        final File file = fileQueryGateway.findById(event.getIdentifier()).orElseThrow();
        fileCreatedIntegrationProducer.produce(FileIntegrationMessage.of(file));

    }

}
