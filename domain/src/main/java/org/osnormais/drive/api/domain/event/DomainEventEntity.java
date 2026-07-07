package org.osnormais.drive.api.domain.event;

import org.osnormais.drive.api.domain.Entity;
import org.osnormais.drive.api.domain.Identifier;

public record DomainEventEntity(String type, String id) {

    public static DomainEventEntity of(final Entity<?> entity) {
        return new DomainEventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

    public static <E extends Entity<?>, I extends Identifier<?>> DomainEventEntity of(
            final Class<E> type,
            final I id) {
        return new DomainEventEntity(type.getSimpleName(), id.getStringValue());
    }

}
