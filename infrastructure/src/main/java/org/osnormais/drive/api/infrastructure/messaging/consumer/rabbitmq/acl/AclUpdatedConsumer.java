package org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.acl;

import java.util.Set;

import org.osnormais.drive.api.application.usecase.acl.entry.inherited.RecalculateInheritedAclEntryInput;
import org.osnormais.drive.api.application.usecase.acl.entry.inherited.RecalculateInheritedAclEntryUseCase;
import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.MessageProducer;
import org.springframework.messaging.Message;

public class AclUpdatedConsumer extends RabbitMQMessageConsumer<AclDomainEventMessage> {

    private final RecalculateInheritedAclEntryUseCase recalculateInheritedAclEntryUseCase;

    public AclUpdatedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<Message<AclDomainEventMessage>> errorMessageProducer,
            final RecalculateInheritedAclEntryUseCase recalculateInheritedAclEntryUseCase) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.recalculateInheritedAclEntryUseCase = recalculateInheritedAclEntryUseCase;
    }

    @Override
    public void consume(final Message<AclDomainEventMessage> message) {
        recalculateInheritedAclEntryUseCase
                .execute(new RecalculateInheritedAclEntryInput(message.getPayload().aclId()));
    }

}
