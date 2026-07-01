package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file;

import org.osnormais.drive.api.infrastructure.file.data.message.command.FinalizeFilePublicationCommand;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class FinalizeFilePublicationCommandProducer extends SpringCloudMessageProducer<FinalizeFilePublicationCommand> {

    private static final String BINDING_NAME = "finalizeFilePublicationCommand-out-0";

    public FinalizeFilePublicationCommandProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}