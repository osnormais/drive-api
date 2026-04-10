package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl;

import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;

public class AclDirectEntryGrantedProducer extends SpringCloudMessageProducer<AclDomainEventMessage> {

    private static final String BINDING_NAME = "aclDirectEntryGranted-out-0";

    public AclDirectEntryGrantedProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}
