package org.osnormais.drive.api.infrastructure.acl.event;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.domain.acl.event.AclUpdatedEvent;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl.AclUpdatedProducer;
import org.springframework.stereotype.Component;

@Component
public class AclUpdatedEventHandler extends DomainEventHandler<AclUpdatedEvent> {

    private final AclUpdatedProducer aclUpdatedProducer;

    protected AclUpdatedEventHandler(final AclUpdatedProducer aclUpdatedProducer) {
        super(AclUpdatedEvent.eventKey());
        this.aclUpdatedProducer = requireNonNull(aclUpdatedProducer);
    }

    @Override
    public void handle(final AclUpdatedEvent event) {
        aclUpdatedProducer.produce(new AclDomainEventMessage(event.getIdentifier().getValue()));
    }

}
