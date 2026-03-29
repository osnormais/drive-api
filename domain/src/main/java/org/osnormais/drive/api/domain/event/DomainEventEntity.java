package org.osnormais.drive.api.domain.event;

import org.osnormais.drive.api.domain.Entity;

public record DomainEventEntity(String type, String id) {

    public static DomainEventEntity of(final Entity<?> entity) {
        return new DomainEventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

}
