package org.osnormais.drive.api.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.osnormais.drive.api.application.usecase.acl.entry.inherited.RecalculateInheritedAclEntryUseCase;
import org.osnormais.drive.api.application.usecase.file.publication.finalize.FinalizeFilePublicationUseCase;
import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.file.data.message.command.FinalizeFilePublicationCommand;
import org.osnormais.drive.api.infrastructure.file.data.message.integration.storage.StorageFileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.acl.AclUpdatedConsumer;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.file.FinalizeFilePublicationCommandConsumer;
import org.osnormais.drive.api.infrastructure.messaging.consumer.rabbitmq.file.StorageFilePublicationFinalizedIntegrationConsumer;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl.AclUpdatedErrorProducer;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file.FinalizeFilePublicationCommandErrorProducer;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file.FinalizeFilePublicationCommandProducer;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file.StorageFilePublicationFinalizedIntegrationErrorProducer;
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

    @Bean
    Consumer<Message<StorageFileIntegrationMessage>> storageFilePublicationFinalizedIntegrationConsumer(
            @Value("${application.messaging.public.consumer.storage-file-publication-finalized.max-attempts}") final Long maxRetryAttempts,
            StorageFilePublicationFinalizedIntegrationErrorProducer errorMessageProducer,
            FinalizeFilePublicationCommandProducer finalizeFilePublicationCommandProducer) {
        return new StorageFilePublicationFinalizedIntegrationConsumer(
                maxRetryAttempts,
                errorMessageProducer,
                finalizeFilePublicationCommandProducer);
    }

    @Bean
    Consumer<Message<FinalizeFilePublicationCommand>> finalizeFilePublicationCommandConsumer(
            @Value("${application.messaging.private.consumer.finalize-file-publication-command.max-attempts}") final Long maxRetryAttempts,
            FinalizeFilePublicationCommandErrorProducer errorMessageProducer,
            FinalizeFilePublicationUseCase finalizeFilePublicationUseCase) {
        return new FinalizeFilePublicationCommandConsumer(
                maxRetryAttempts,
                errorMessageProducer,
                finalizeFilePublicationUseCase);
    }

}
