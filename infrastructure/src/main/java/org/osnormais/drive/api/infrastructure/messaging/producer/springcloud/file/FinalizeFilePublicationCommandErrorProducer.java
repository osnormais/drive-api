package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.file;

import org.osnormais.drive.api.infrastructure.file.data.message.command.FinalizeFilePublicationCommand;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class FinalizeFilePublicationCommandErrorProducer
        extends SpringCloudMessageProducer<Message<FinalizeFilePublicationCommand>> {

    private static final String BINDING_NAME = "finalizeFilePublicationCommandError-out-0";

    public FinalizeFilePublicationCommandErrorProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}