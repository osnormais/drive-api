package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file;

import org.osnormais.drive.api.infrastructure.file.data.message.integration.storage.StorageFileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class StorageFilePublicationFinalizedIntegrationErrorProducer
        extends SpringCloudMessageProducer<Message<StorageFileIntegrationMessage>> {

    private static final String BINDING_NAME = "storageFilePublicationFinalizedIntegrationError-out-0";

    public StorageFilePublicationFinalizedIntegrationErrorProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}
