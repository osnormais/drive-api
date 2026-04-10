package org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.acl;

import java.util.Set;

import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.MessageProducer;
import org.springframework.messaging.Message;

public class AclDirectEntryGrantedConsumer extends RabbitMQMessageConsumer<AclDomainEventMessage> {

    public AclDirectEntryGrantedConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<AclDomainEventMessage> errorMessageProducer) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
    }

    @Override
    public void consume(final Message<AclDomainEventMessage> message) {
        System.out.println("Consumed message: " + message.getPayload());
    }

}
