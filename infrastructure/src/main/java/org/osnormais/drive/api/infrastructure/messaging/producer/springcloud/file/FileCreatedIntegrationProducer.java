package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file;

import org.osnormais.drive.api.infrastructure.file.data.message.integration.drive.DriveFileIntegrationMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class FileCreatedIntegrationProducer extends SpringCloudMessageProducer<DriveFileIntegrationMessage> {

    private static final String BINDING_NAME = "fileCreatedIntegration-out-0";

    public FileCreatedIntegrationProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}
