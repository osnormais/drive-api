package org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.example;

import java.util.Set;

import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.MessageProducer;
import org.springframework.messaging.Message;

public class ExampleConsumer extends RabbitMQMessageConsumer<String> {

    public ExampleConsumer(
            final Long maxRetryAttempts,
            final MessageProducer<String> errorMessageProducer) {
        super(maxRetryAttempts, errorMessageProducer, Set.of(RuntimeException.class));
    }

    @Override
    public void consume(final Message<String> message) {

    }

}
