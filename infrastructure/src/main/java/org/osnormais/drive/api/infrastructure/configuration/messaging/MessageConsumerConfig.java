package org.osnormais.drive.api.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.acl.AclDirectEntryGrantedConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl.AclDirectEntryGrantedErrorProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
public class MessageConsumerConfig {

    @Bean
    Consumer<Message<AclDomainEventMessage>> aclDirectEntryGrantedConsumer(
            @Value("${application.messaging.private.consumer.acl-direct-entry-granted.max-attempts}") final Long maxAttempts,
            AclDirectEntryGrantedErrorProducer errorMessageProducer) {
        return new AclDirectEntryGrantedConsumer(
                maxAttempts,
                errorMessageProducer);
    }

}
