package org.osnormais.drive.api.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.osnormais.drive.api.application.usecase.acl.entry.inherited.RecalculateInheritedAclEntryUseCase;
import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.acl.AclUpdatedConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl.AclUpdatedErrorProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
public class MessageConsumerConfig {

    @Bean
    Consumer<Message<AclDomainEventMessage>> aclUpdatedConsumer(
            @Value("${application.messaging.private.consumer.acl-updated.max-attempts}") final Long maxAttempts,
            AclUpdatedErrorProducer errorMessageProducer,
            RecalculateInheritedAclEntryUseCase recalculateInheritedAclEntryUseCase) {
        return new AclUpdatedConsumer(
                maxAttempts,
                errorMessageProducer,
                recalculateInheritedAclEntryUseCase);
    }

}
