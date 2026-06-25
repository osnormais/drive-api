package org.osnormais.drive.api.infrastructure.file.event;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.event.FilePublicationInitiedEvent;
import org.osnormais.drive.api.infrastructure.file.data.message.FileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file.FilePublicationInitiatedIntegrationProducer;
import org.springframework.stereotype.Component;

@Component
public class FilePublicationInitiatedIntegrationHandler extends DomainEventHandler<FilePublicationInitiedEvent> {

    private final FileQueryGateway fileQueryGateway;
    private final FilePublicationInitiatedIntegrationProducer filePublicationInitiedIntegrationProducer;

    protected FilePublicationInitiatedIntegrationHandler(
            final FileQueryGateway fileQueryGateway,
            final FilePublicationInitiatedIntegrationProducer filePublicationInitiedIntegrationProducer) {
        super(FilePublicationInitiedEvent.eventKey());
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.filePublicationInitiedIntegrationProducer = requireNonNull(filePublicationInitiedIntegrationProducer);
    }

    @Override
    public void handle(final FilePublicationInitiedEvent event) {

        final File file = fileQueryGateway.findById(event.getIdentifier()).orElseThrow();
        filePublicationInitiedIntegrationProducer.produce(FileIntegrationMessage.of(file));

    }

}
