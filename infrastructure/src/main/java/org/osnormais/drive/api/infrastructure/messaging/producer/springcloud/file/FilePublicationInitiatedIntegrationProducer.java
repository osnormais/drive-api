package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file;

import org.osnormais.drive.api.infrastructure.file.data.message.integration.drive.DriveFileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class FilePublicationInitiatedIntegrationProducer extends SpringCloudMessageProducer<DriveFileIntegrationMessage> {

    private static final String BINDING_NAME = "filePublicationInitiatedIntegration-out-0";

    public FilePublicationInitiatedIntegrationProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}
