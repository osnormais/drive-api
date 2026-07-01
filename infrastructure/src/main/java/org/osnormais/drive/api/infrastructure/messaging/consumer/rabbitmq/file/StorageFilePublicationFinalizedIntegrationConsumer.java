package org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.file;

import static java.util.Objects.requireNonNull;

import java.util.Set;

import org.osnormais.drive.api.infrastructure.file.data.message.command.FinalizeFilePublicationCommand;
import org.osnormais.drive.api.infrastructure.file.data.message.integration.storage.StorageFileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.MessageProducer;
import org.springframework.messaging.Message;

public class StorageFilePublicationFinalizedIntegrationConsumer
        extends RabbitMQMessageConsumer<StorageFileIntegrationMessage> {

    private final MessageProducer<FinalizeFilePublicationCommand> finalizeFilePublicationCommandProducer;

    public StorageFilePublicationFinalizedIntegrationConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<Message<StorageFileIntegrationMessage>> errorMessageProducer,
            final MessageProducer<FinalizeFilePublicationCommand> finalizeFilePublicationCommandProducer) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.finalizeFilePublicationCommandProducer = requireNonNull(finalizeFilePublicationCommandProducer);
    }

    @Override
    public void consume(final Message<StorageFileIntegrationMessage> message) {
        finalizeFilePublicationCommandProducer.produce(new FinalizeFilePublicationCommand(
                message.getPayload().id(),
                message.getPayload().publicationStatus(),
                message.getPayload().publicationError()));
    }

}