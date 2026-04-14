package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl;

import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class AclUpdatedErrorProducer extends SpringCloudMessageProducer<AclDomainEventMessage> {

    private static final String BINDING_NAME = "aclUpdatedError-out-0";

    public AclUpdatedErrorProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}
