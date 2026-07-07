package org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.file;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.Set;

import org.osnormais.drive.api.application.usecase.file.publication.finalize.FinalizeFilePublicationInput;
import org.osnormais.drive.api.application.usecase.file.publication.finalize.FinalizeFilePublicationUseCase;
import org.osnormais.drive.api.infrastructure.file.data.message.command.FinalizeFilePublicationCommand;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.MessageProducer;
import org.springframework.messaging.Message;

public class FinalizeFilePublicationCommandConsumer
        extends RabbitMQMessageConsumer<FinalizeFilePublicationCommand> {

    private final FinalizeFilePublicationUseCase finalizeFilePublicationUseCase;

    public FinalizeFilePublicationCommandConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<Message<FinalizeFilePublicationCommand>> errorMessageProducer,
            final FinalizeFilePublicationUseCase finalizeFilePublicationUseCase) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.finalizeFilePublicationUseCase = requireNonNull(finalizeFilePublicationUseCase);
    }

    @Override
    public void consume(final Message<FinalizeFilePublicationCommand> message) {
        finalizeFilePublicationUseCase.execute(new FinalizeFilePublicationInput(
                message.getPayload().fileId(),
                message.getPayload().status(),
                Optional.ofNullable(message.getPayload().errorMessage())));
    }

}