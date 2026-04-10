package org.osnormais.drive.api.domain.acl.event;

import java.time.Instant;
import java.util.List;

import org.osnormais.drive.api.domain.acl.Acl;
import org.osnormais.drive.api.domain.acl.AclId;
import org.osnormais.drive.api.domain.event.DomainEvent;
import org.osnormais.drive.api.domain.event.DomainEventEntity;
import org.osnormais.drive.api.domain.user.User;

public class AclDirectEntryGrantedEvent extends DomainEvent<AclId> {

    private static final Class<Acl> ENTITY_CLASS = Acl.class;
    private static final String SUB_RESOURCE = "direct_entry";
    private static final String ACTION = "granted";

    private AclDirectEntryGrantedEvent(
            final Acl acl,
            final Instant occurredAt,
            final List<DomainEventEntity> relatedEntities) {
        super(
                acl,
                SUB_RESOURCE,
                ACTION,
                occurredAt,
                relatedEntities);
    }

    public static AclDirectEntryGrantedEvent create(final Acl acl) {
        return new AclDirectEntryGrantedEvent(
                acl,
                Instant.now(),
                List.of(
                        DomainEventEntity.of(acl),
                        DomainEventEntity.of(User.class, acl.getResource().owner()),
                        DomainEventEntity.of(
                                acl.getResource().resourceType().getEntityClass(),
                                acl.getResource().resourceId())));
    }

    public static String eventKey() {
        return DomainEvent.key(ENTITY_CLASS, null, ACTION);
    }
}
