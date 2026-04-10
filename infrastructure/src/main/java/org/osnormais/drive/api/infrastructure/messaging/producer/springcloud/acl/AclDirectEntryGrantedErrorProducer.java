package org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl;

import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class AclDirectEntryGrantedErrorProducer extends SpringCloudMessageProducer<AclDomainEventMessage> {

    private static final String BINDING_NAME = "aclDirectEntryGrantedError-out-0";

    public AclDirectEntryGrantedErrorProducer(final StreamBridge streamBridge) {
        super(streamBridge, BINDING_NAME);
    }

}
