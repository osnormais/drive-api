package org.osnormais.drive.api.infrastructure.acl.event;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.domain.acl.event.AclDirectEntryGrantedEvent;
import org.osnormais.drive.api.domain.event.DomainEventHandler;
import org.osnormais.drive.api.domain.file.event.FileCreatedEvent;
import org.osnormais.drive.api.infrastructure.acl.data.message.AclDomainEventMessage;
import org.osnormais.drive.api.infrastructure.messaging.producer.springcloud.acl.AclDirectEntryGrantedProducer;
import org.springframework.stereotype.Component;

@Component
public class AclDirectEntryGrantedEventHandler extends DomainEventHandler<AclDirectEntryGrantedEvent> {

    private final AclDirectEntryGrantedProducer aclDirectEntryGrantedProducer;

    protected AclDirectEntryGrantedEventHandler(final AclDirectEntryGrantedProducer aclDirectEntryGrantedProducer) {
        super(FileCreatedEvent.eventKey());
        this.aclDirectEntryGrantedProducer = requireNonNull(aclDirectEntryGrantedProducer);
    }

    @Override
    public void handle(final AclDirectEntryGrantedEvent event) {
        aclDirectEntryGrantedProducer.produce(new AclDomainEventMessage(event.getIdentifier().getValue()));
    }

}
